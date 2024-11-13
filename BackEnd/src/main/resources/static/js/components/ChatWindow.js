import NotificationHandler from '../utils/NotificationHandler.js';

const ChatWindow = async (chatId) => {
  let messages = await API.get(`/chat/messages/${chatId}`);

  const socket = new SockJS('/chat'); // Connect to SockJS endpoint
  const stompClient = Stomp.over(socket);

  stompClient.connect({}, () => {
    stompClient.subscribe(`/queue/messages/${chatId}`, (messageOutput) => {
      const message = JSON.parse(messageOutput.body);
      NotificationHandler.show(message.content);
      document.getElementById('messages').innerHTML += `<p>${message.content}</p>`;
    });
  });

  return `
    <div id="messages">
      ${messages.map(m => `<p>${m.content}</p>`).join('')}
    </div>
    <input id="messageInput" placeholder="Type a message..." />
    <button onclick="sendMessage(${chatId})">Send</button>
  `;
};

window.sendMessage = (chatId) => {
  const messageContent = document.getElementById('messageInput').value;
  stompClient.send(`/app/chat`, {}, JSON.stringify({ content: messageContent, chatId }));
  document.getElementById('messageInput').value = '';
};

export default ChatWindow;
