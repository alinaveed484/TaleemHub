import API from '../utils/API.js';
import TeacherDetails from './TeacherDetails.js';

const MentorshipList = async () => {
  const teachers = await API.get('/student/select_mentor/display_teachers?subject=Math');
  return `
    <h2>Available Teachers</h2>
    <ul>
      ${teachers.map(t => `<li onclick="viewTeacherDetails(${t.id})">${t.name}</li>`).join('')}
    </ul>
    <div id="teacherDetails"></div>
  `;
};

window.viewTeacherDetails = async (id) => {
  document.getElementById('teacherDetails').innerHTML = await TeacherDetails(id);
};

export default MentorshipList;
