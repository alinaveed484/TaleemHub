'use strict';

const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

let stompClient = null;
let personDTO = {}; // Store user data in an object
let selectedUserId = null;
const chatHistories = new Map(); // To store local chat history for each user

function connect(event) {
    // Capture form data based on PersonDTO fields
    personDTO.id = parseInt(document.querySelector('#id').value.trim());
    personDTO.firstName = document.querySelector('#firstName').value.trim();
    personDTO.lastName = document.querySelector('#lastName').value.trim();
    personDTO.status = document.querySelector('#status').value === 'true';
    personDTO.type = document.querySelector('#type').value.trim();

    if (personDTO.firstName && personDTO.lastName) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

function onConnected() {
    stompClient.subscribe(`/user/${personDTO.id}/queue/messages`, onMessageReceived);

    if (selectedUserId) {
            stompClient.subscribe(`/user/${personDTO.id}/${selectedUserId}/queue/messages`, onMessageReceived);
            stompClient.subscribe(`/user/${selectedUserId}/${personDTO.id}/queue/messages`, onMessageReceived);
    }
    // Register the connected user
    stompClient.send("/user.addUser",
        {},
        JSON.stringify({
            id: personDTO.id,
            firstName: personDTO.firstName,
            lastName: personDTO.lastName,
            status: personDTO.status,
            type: personDTO.type
        })
    );
    console.log("Connected as:", personDTO);

    document.querySelector('#connected-user-fullname').textContent = `${personDTO.firstName} ${personDTO.lastName}`;
    findAndDisplayConnectedUsers().then();
}

async function findAndDisplayConnectedUsers() {
    const personID = personDTO.id;  // Replace with actual person ID
    const person_type = personDTO.type;  // Replace with actual person type (e.g., "Teacher" or "Student")

    const url = `/users?personID=${personID}&person_type=${encodeURIComponent(person_type)}`;

    try {
        const connectedUsersResponse = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!connectedUsersResponse.ok) {
            throw new Error(`Error fetching users: ${connectedUsersResponse.status}`);
        }

        let connectedUsers = await connectedUsersResponse.json();
        connectedUsers = connectedUsers.filter(user => user.id !== personID); // Filter out the current user
        const connectedUsersList = document.getElementById('connectedUsers');
        connectedUsersList.innerHTML = '';

        connectedUsers.forEach(user => {
            appendUserElement(user, connectedUsersList);
            if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
                const separator = document.createElement('li');
                separator.classList.add('separator');
                connectedUsersList.appendChild(separator);
            }
        });
    } catch (error) {
        console.error("Failed to fetch connected users:", error);
    }
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.id;

    const userImage = document.createElement('img');
    userImage.src = '../img/user_icon.png';
    userImage.alt = `${user.firstName} ${user.lastName}`;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = `${user.firstName} ${user.lastName}`;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = parseInt(clickedUser.getAttribute('id'));
    fetchAndDisplayUserChat().then();
    //fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';
}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === personDTO.id) {
        messageContainer.classList.add('sender'); // Messages from current user appear on the right
    } else {
        messageContainer.classList.add('receiver'); // Messages from others appear on the left
    }
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
    chatArea.scrollTop = chatArea.scrollHeight; // Scroll to the latest message
}
//
//async function fetchAndDisplayUserChat() {
//    const userChatResponse = await fetch(`/chat/messages/${personDTO.id}/${selectedUserId}`);
//    const userChat = await userChatResponse.json();
//
//    // Merge local messages with fetched messages
//    const localMessages = chatHistories.get(selectedUserId) || [];
//    const allMessages = [...userChat, ...localMessages];
//
//    chatArea.innerHTML = '';
//    allMessages.forEach(chat => {
//        displayMessage(chat.senderId, chat.content);
//    });
//
//    // Scroll to the latest message
//    chatArea.scrollTop = chatArea.scrollHeight;
//}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/chat/messages/${personDTO.id}/${selectedUserId}`);
    const serverMessages = await userChatResponse.json();

    // Update local chat history with server messages
    chatHistories.set(selectedUserId, serverMessages);

    // Display all messages for the active chat
    chatArea.innerHTML = '';
    serverMessages.forEach(chat => {
        displayMessage(chat.senderId, chat.content);
    });

    // Scroll to the latest message
    chatArea.scrollTop = chatArea.scrollHeight;
}


function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            senderId: personDTO.id,
            recipientId: selectedUserId,
            content: messageContent,
            type: "CHAT",
            timeStamp: new Date().toISOString(), // Use ISO format for LocalDateTime
        };
        stompClient.send("/chat/send_msg", {}, JSON.stringify(chatMessage));

        // Display the message immediately
        displayMessage(personDTO.id, chatMessage.content);

        // Update local chat history
        if (!chatHistories.has(selectedUserId)) {
            chatHistories.set(selectedUserId, []);
        }
        chatHistories.get(selectedUserId).push(chatMessage);

        messageInput.value = '';
    }
    event.preventDefault();
}

async function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);

    // Update the local chat history
    if (!chatHistories.has(message.senderId)) {
        chatHistories.set(message.senderId, []);
    }
    chatHistories.get(message.senderId).push(message);

    // If the message is for the active chat, refresh the chat area
    if (selectedUserId && (selectedUserId === message.senderId || selectedUserId === message.recipientId)) {
        // Clear chat area and reload messages for the active chat
        chatArea.innerHTML = '';
        const activeChatMessages = chatHistories.get(selectedUserId) || [];
        activeChatMessages.forEach(chat => {
            displayMessage(chat.senderId, chat.content);
        });

        // Add the new message if it's not already displayed
        displayMessage(message.senderId, message.content);

        // Scroll to the latest message
        chatArea.scrollTop = chatArea.scrollHeight;
    }
}



function onLogout() {
    stompClient.send("/user.disconnectUser",
        {},
        JSON.stringify({id: personDTO.id, firstName: personDTO.firstName, lastName: personDTO.lastName, status: false})
    );
    window.location.reload();
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();

