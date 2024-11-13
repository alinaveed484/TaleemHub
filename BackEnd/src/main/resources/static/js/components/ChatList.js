import API from '../utils/API.js';
import ChatWindow from './ChatWindow.js';

const ChatList = async () => {
  // Example Person object - replace with actual data as needed
  const person = {
    id: 1,           // Replace with actual ID
    firstName: "John", // Replace with actual first name
    lastName: "Doe",  // Replace with actual last name
    status: true      // Replace with actual status if needed
  };

  let chats;
  try {
    // Send a POST request to /users with the Person object
    chats = await API.post('/users', person);
  } catch (error) {
    console.error("Failed to fetch users:", error);
    return `<p>Error loading chats. Please try again later.</p>`;
  }

  if (!Array.isArray(chats)) {
    return `<p>No active chats available.</p>`;
  }

  return `
    <h2>Active Chats</h2>
    <ul>
      ${chats.map(c => `<li onclick="openChat(${c.id})">${c.name}</li>`).join('')}
    </ul>
    <div id="chatWindow"></div>
  `;
};

window.openChat = async (chatId) => {
  document.getElementById('chatWindow').innerHTML = await ChatWindow(chatId);
};

export default ChatList;
