import MentorshipList from './components/MentorshipList.js';
import ChatList from './components/ChatList.js';

const app = document.getElementById('app');
const render = (component) => app.innerHTML = component;

const loadMentorship = () => render(MentorshipList());
const loadChat = () => render(ChatList());

app.innerHTML = `
  <nav>
    <button onclick="loadMentorship()">Mentorship</button>
    <button onclick="loadChat()">Chat</button>
  </nav>
  <div id="content"></div>
`;

window.loadMentorship = loadMentorship;
window.loadChat = loadChat;
