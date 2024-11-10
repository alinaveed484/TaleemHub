import API from '../utils/API.js';

const TeacherDetails = async (id) => {
  const details = await API.get(`/student/select_mentor/show_teacher_details?teacherID=${id}`);
  return `
    <h3>${details.firstName} ${details.lastName}</h3>
    <p>Subject: ${details.subjectSpecialization}</p>
    <p>Experience: ${details.yearsExperience} years</p>
    <button onclick="sendMentorRequest(${id})">Request Mentorship</button>
  `;
};

window.sendMentorRequest = async (teacherID) => {
  const result = await API.post(`/student/select_mentor/send_mentor_request`, { teacherID });
  alert(result.message || 'Request sent successfully');
};

export default TeacherDetails;
