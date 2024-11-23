////'use strict';
////
////const mentorSelectionPage = document.querySelector('#mentor-selection-page');
////const mentorDetailsPage = document.querySelector('#mentor-details-page');
////const mentorSelectionForm = document.querySelector('#mentorSelectionForm');
////const showTeacherDetailsBtn = document.querySelector('#showTeacherDetailsBtn');
////const sendMentorRequestBtn = document.querySelector('#sendMentorRequestBtn');
////const backToSelectionBtn = document.querySelector('#backToSelectionBtn');
////const teacherDetailsContainer = document.querySelector('#teacherDetails');
////
////let selectedTeacherID = null;
////let studentID = null;
////
////// Fetch and display teacher details
////showTeacherDetailsBtn.addEventListener('click', async () => {
////    const teacherIDInput = document.querySelector('#teacherID').value.trim();
////    const studentIDInput = document.querySelector('#studentID').value.trim();
////
////    if (!teacherIDInput || !studentIDInput) {
////        alert("Both Student ID and Teacher ID are required!");
////        return;
////    }
////
////    selectedTeacherID = parseInt(teacherIDInput);
////    studentID = parseInt(studentIDInput);
////
////    try {
////        const response = await fetch(`/student/select_mentor/show_teacher_details?teacherID=${selectedTeacherID}`);
////        if (!response.ok) {
////            throw new Error(`Failed to fetch teacher details: ${response.statusText}`);
////        }
////
////        const teacherDetails = await response.json();
////        displayTeacherDetails(teacherDetails);
////        mentorSelectionPage.classList.add('hidden');
////        mentorDetailsPage.classList.remove('hidden');
////    } catch (error) {
////        console.error("Error fetching teacher details:", error);
////        alert("Unable to fetch teacher details. Please try again.");
////    }
////});
////
////// Send mentor request
////sendMentorRequestBtn.addEventListener('click', async () => {
////    if (!selectedTeacherID || !studentID) {
////        alert("Teacher ID and Student ID are required to send a mentor request!");
////        return;
////    }
////
////    try {
////        const response = await fetch(`/student/select_mentor/send_mentor_request?teacherID=${selectedTeacherID}&studentID=${studentID}`);
////        if (!response.ok) {
////            throw new Error(`Failed to send mentor request: ${response.statusText}`);
////        }
////
////        const result = await response.json();
////        alert("Mentor request sent successfully!");
////        console.log("Mentor request response:", result);
////        backToSelection();
////    } catch (error) {
////        console.error("Error sending mentor request:", error);
////        alert("Unable to send mentor request. Please try again.");
////    }
////});
////
////// Go back to mentor selection page
////backToSelectionBtn.addEventListener('click', backToSelection);
////
////function backToSelection() {
////    mentorDetailsPage.classList.add('hidden');
////    mentorSelectionPage.classList.remove('hidden');
////    teacherDetailsContainer.innerHTML = ''; // Clear previous teacher details
////}
////
////// Helper function to display teacher details
////function displayTeacherDetails(details) {
////    teacherDetailsContainer.innerHTML = `
////        <p><strong>Teacher ID:</strong> ${details.teacherId}</p>
////        <p><strong>Name:</strong> ${details.firstName} ${details.lastName}</p>
////        <p><strong>Qualification:</strong> ${details.qualification}</p>
////        <p><strong>Subject Specialization:</strong> ${details.subjectSpecialization}</p>
////        <p><strong>Years of Experience:</strong> ${details.yearsExperience}</p>
////        <p><strong>Email:</strong> ${details.email}</p>
////    `;
////}
//
//'use strict';
//
//const mentorSelectionPage = document.querySelector('#mentor-selection-page');
//const mentorDetailsPage = document.querySelector('#mentor-details-page');
//const mentorSelectionForm = document.querySelector('#mentorSelectionForm');
//const showTeachersBtn = document.querySelector('#showTeachersBtn');
//const sendMentorRequestBtn = document.querySelector('#sendMentorRequestBtn');
//const backToSelectionBtn = document.querySelector('#backToSelectionBtn');
//const teacherDetailsContainer = document.querySelector('#teacherDetails');
//const teachersList = document.querySelector('#teachersList');
//const teacherListContainer = document.querySelector('#teacherListContainer');
//
//let selectedTeacherID = null;
//let studentID = null;
//
//// Fetch and display teachers based on subject
//showTeachersBtn.addEventListener('click', async () => {
//    const subject = document.querySelector('#subject').value.trim();
//    const studentIDInput = document.querySelector('#studentID').value.trim();
//
//    if (!subject || !studentIDInput) {
//        alert("Both Student ID and Subject are required!");
//        return;
//    }
//
//    studentID = parseInt(studentIDInput);
//
//    try {
//        const response = await fetch(`/student/select_mentor/display_teachers?subject=${encodeURIComponent(subject)}`);
//        if (!response.ok) {
//            throw new Error(`Failed to fetch teachers: ${response.statusText}`);
//        }
//
//        const teachers = await response.json();
//        displayTeachers(teachers);
//    } catch (error) {
//        console.error("Error fetching teachers:", error);
//        alert("Unable to fetch teachers. Please try again.");
//    }
//});
//
//// Display list of teachers
//function displayTeachers(teachers) {
//    teacherListContainer.innerHTML = ''; // Clear previous list
//    if (teachers.length === 0) {
//        teacherListContainer.innerHTML = '<li>No teachers found for the selected subject.</li>';
//        return;
//    }
//
//    teachers.forEach(teacher => {
//        const listItem = document.createElement('li');
//        listItem.textContent = `${teacher.firstName} ${teacher.lastName} - ${teacher.subjectSpecialization}`;
//        listItem.dataset.teacherId = teacher.id; // Store teacher ID for later reference
//        listItem.addEventListener('click', () => fetchTeacherDetails(teacher.id));
//        teacherListContainer.appendChild(listItem);
//    });
//
//    teachersList.classList.remove('hidden');
//}
//
//// Fetch and display detailed information for a specific teacher
//async function fetchTeacherDetails(teacherID) {
//    selectedTeacherID = teacherID;
//
//    try {
//        const response = await fetch(`/student/select_mentor/show_teacher_details?teacherID=${selectedTeacherID}`);
//        if (!response.ok) {
//            throw new Error(`Failed to fetch teacher details: ${response.statusText}`);
//        }
//
//        const teacherDetails = await response.json();
//        displayTeacherDetails(teacherDetails);
//        mentorSelectionPage.classList.add('hidden');
//        mentorDetailsPage.classList.remove('hidden');
//    } catch (error) {
//        console.error("Error fetching teacher details:", error);
//        alert("Unable to fetch teacher details. Please try again.");
//    }
//}
//
//// Send mentor request
//sendMentorRequestBtn.addEventListener('click', async () => {
//    if (!selectedTeacherID || !studentID) {
//        alert("Teacher ID and Student ID are required to send a mentor request!");
//        return;
//    }
//
//    try {
//        const response = await fetch(`/student/select_mentor/send_mentor_request?teacherID=${selectedTeacherID}&studentID=${studentID}`);
//        if (!response.ok) {
//            throw new Error(`Failed to send mentor request: ${response.statusText}`);
//        }
//
//        alert("Mentor request sent successfully!");
//        backToSelection();
//    } catch (error) {
//        console.error("Error sending mentor request:", error);
//        alert("Unable to send mentor request. Please try again.");
//    }
//});
//
//// Go back to mentor selection page
//backToSelectionBtn.addEventListener('click', backToSelection);
//
//function backToSelection() {
//    mentorDetailsPage.classList.add('hidden');
//    mentorSelectionPage.classList.remove('hidden');
//    teacherDetailsContainer.innerHTML = ''; // Clear previous teacher details
//    teachersList.classList.add('hidden'); // Hide teacher list
//}
//
//// Helper function to display teacher details
//function displayTeacherDetails(details) {
//    teacherDetailsContainer.innerHTML = `
//        <p><strong>Teacher ID:</strong> ${details.teacherId}</p>
//        <p><strong>Name:</strong> ${details.firstName} ${details.lastName}</p>
//        <p><strong>Qualification:</strong> ${details.qualification}</p>
//        <p><strong>Subject Specialization:</strong> ${details.subjectSpecialization}</p>
//        <p><strong>Years of Experience:</strong> ${details.yearsExperience}</p>
//        <p><strong>Email:</strong> ${details.email}</p>
//    `;
//}
//
//
//'use strict';
//
//const mentorSelectionPage = document.querySelector('#mentor-selection-page');
//const mentorDetailsPage = document.querySelector('#mentor-details-page');
//const mentorSelectionForm = document.querySelector('#mentorSelectionForm');
//const showTeachersBtn = document.querySelector('#showTeachersBtn');
//const sendMentorRequestBtn = document.querySelector('#sendMentorRequestBtn');
//const backToSelectionBtn = document.querySelector('#backToSelectionBtn');
//const teacherDetailsContainer = document.querySelector('#teacherDetails');
//const teachersList = document.querySelector('#teachersList');
//const teacherListContainer = document.querySelector('#teacherListContainer');
//
//let selectedTeacherID = null;
//let studentID = null;
//
//// Fetch and display teachers based on subject
//showTeachersBtn.addEventListener('click', async () => {
//    const subject = document.querySelector('#subject').value.trim();
//    const studentIDInput = document.querySelector('#studentID').value.trim();
//
//    if (!subject || !studentIDInput) {
//        alert("Both Student ID and Subject are required!");
//        return;
//    }
//
//    studentID = parseInt(studentIDInput);
//
//    try {
//        const response = await fetch(`/student/select_mentor/display_teachers?subject=${encodeURIComponent(subject)}`);
//        if (!response.ok) {
//            throw new Error(`Failed to fetch teachers: ${response.statusText}`);
//        }
//
//        const teachers = await response.json();
//        displayTeachers(teachers);
//    } catch (error) {
//        console.error("Error fetching teachers:", error);
//        alert("Unable to fetch teachers. Please try again.");
//    }
//});
//
//// Display list of teachers
//function displayTeachers(teachers) {
//    teacherListContainer.innerHTML = ''; // Clear previous list
//    if (teachers.length === 0) {
//        teacherListContainer.innerHTML = '<li>No teachers found for the selected subject.</li>';
//        return;
//    }
//
//    teachers.forEach(teacher => {
//        const listItem = document.createElement('li');
//        listItem.classList.add('teacher-card');
//        listItem.innerHTML = `
//            <img src="${teacher.profilePhotoURL || 'placeholder.png'}" alt="${teacher.firstName} ${teacher.lastName}" class="teacher-photo">
//            <div class="teacher-info">
//                <p><strong>${teacher.firstName} ${teacher.lastName}</strong></p>
//                <p>${teacher.subjectSpecialization}</p>
//            </div>
//        `;
//        listItem.addEventListener('click', () => fetchTeacherDetails(teacher));
//        teacherListContainer.appendChild(listItem);
//    });
//
//    teachersList.classList.remove('hidden');
//}
//
//// Fetch and display detailed information for a specific teacher
//async function fetchTeacherDetails(teacher) {
//    selectedTeacherID = teacher.id;
//
//    try {
//        const response = await fetch(`/student/select_mentor/show_teacher_details?teacherID=${selectedTeacherID}`);
//        if (!response.ok) {
//            throw new Error(`Failed to fetch teacher details: ${response.statusText}`);
//        }
//
//        const teacherDetails = await response.json();
//        displayTeacherDetails(teacherDetails);
//        mentorSelectionPage.classList.add('hidden');
//        mentorDetailsPage.classList.remove('hidden');
//    } catch (error) {
//        console.error("Error fetching teacher details:", error);
//        alert("Unable to fetch teacher details. Please try again.");
//    }
//}
//
//// Send mentor request
//sendMentorRequestBtn.addEventListener('click', async () => {
//    if (!selectedTeacherID || !studentID) {
//        alert("Teacher ID and Student ID are required to send a mentor request!");
//        return;
//    }
//
//    try {
//        const response = await fetch(`/student/select_mentor/send_mentor_request?teacherID=${selectedTeacherID}&studentID=${studentID}`);
//        if (!response.ok) {
//            throw new Error(`Failed to send mentor request: ${response.statusText}`);
//        }
//
//        alert("Mentor request sent successfully!");
//        backToSelection();
//    } catch (error) {
//        console.error("Error sending mentor request:", error);
//        alert("Unable to send mentor request. Please try again.");
//    }
//});
//
//// Go back to mentor selection page
//backToSelectionBtn.addEventListener('click', backToSelection);
//
//function backToSelection() {
//    mentorDetailsPage.classList.add('hidden');
//    mentorSelectionPage.classList.remove('hidden');
//    teacherDetailsContainer.innerHTML = ''; // Clear previous teacher details
//    teachersList.classList.add('hidden'); // Hide teacher list
//}
//
//// Helper function to display teacher details
//function displayTeacherDetails(details) {
//    teacherDetailsContainer.innerHTML = `
//        <p><strong>Teacher ID:</strong> ${details.teacherId}</p>
//        <p><strong>Name:</strong> ${details.firstName} ${details.lastName}</p>
//        <p><strong>Qualification:</strong> ${details.qualification}</p>
//        <p><strong>Subject Specialization:</strong> ${details.subjectSpecialization}</p>
//        <p><strong>Years of Experience:</strong> ${details.yearsExperience}</p>
//        <p><strong>Email:</strong> ${details.email}</p>
//    `;
//}

'use strict';

const mentorSelectionPage = document.querySelector('#mentor-selection-page');
const mentorDetailsPage = document.querySelector('#mentor-details-page');
const mentorSelectionForm = document.querySelector('#mentorSelectionForm');
const showTeachersBtn = document.querySelector('#showTeachersBtn');
const sendMentorRequestBtn = document.querySelector('#sendMentorRequestBtn');
const backToSelectionBtn = document.querySelector('#backToSelectionBtn');
const teacherDetailsContainer = document.querySelector('#teacherDetails');
const teachersList = document.querySelector('#teachersList');
const teacherListContainer = document.querySelector('#teacherListContainer');

let studentUID = null; // Store student UID
let selectedTeacherID = null;

// Fetch Student UID on Page Load
firebase.auth().onAuthStateChanged(user => {
  if (user) {
    studentUID = user.uid; // Retrieve UID
    console.log("Student logged in with UID:", studentUID);
  } else {
    alert("You are not logged in. Redirecting to login page.");
    window.location.href = "/Front/html/auth-login-basic.html"; // Redirect to login page if not logged in
  }
});

// Fetch and display teachers based on subject
showTeachersBtn.addEventListener('click', async () => {
  const subject = document.querySelector('#subject').value.trim();

  if (!subject || !studentUID) {
    alert("Subject is required!");
    return;
  }

  try {
    const response = await fetch(`/student/select_mentor/display_teachers?subject=${encodeURIComponent(subject)}`);
    if (!response.ok) {
      throw new Error(`Failed to fetch teachers: ${response.statusText}`);
    }

    const teachers = await response.json();
    displayTeachers(teachers);
  } catch (error) {
    console.error("Error fetching teachers:", error);
    alert("Unable to fetch teachers. Please try again.");
  }
});

// Display list of teachers
function displayTeachers(teachers) {
  teacherListContainer.innerHTML = ''; // Clear previous list
  if (teachers.length === 0) {
    teacherListContainer.innerHTML = '<li>No teachers found for the selected subject.</li>';
    return;
  }

  teachers.forEach(teacher => {
    const listItem = document.createElement('li');
    listItem.textContent = `${teacher.firstName} ${teacher.lastName} - ${teacher.subjectSpecialization}`;
    listItem.dataset.teacherId = teacher.id; // Store teacher ID for later reference
    listItem.addEventListener('click', () => fetchTeacherDetails(teacher.id));
    teacherListContainer.appendChild(listItem);
  });

  teachersList.classList.remove('hidden');
}

// Fetch and display detailed information for a specific teacher
async function fetchTeacherDetails(teacherID) {
  selectedTeacherID = teacherID;

  try {
    const response = await fetch(`/student/select_mentor/show_teacher_details?teacherID=${selectedTeacherID}`);
    if (!response.ok) {
      throw new Error(`Failed to fetch teacher details: ${response.statusText}`);
    }

    const teacherDetails = await response.json();
    displayTeacherDetails(teacherDetails);
    mentorSelectionPage.classList.add('hidden');
    mentorDetailsPage.classList.remove('hidden');
  } catch (error) {
    console.error("Error fetching teacher details:", error);
    alert("Unable to fetch teacher details. Please try again.");
  }
}

// Send mentor request
sendMentorRequestBtn.addEventListener('click', async () => {
  if (!selectedTeacherID || !studentUID) {
    alert("Teacher ID is required to send a mentor request!");
    return;
  }

  try {
    const response = await fetch(`/student/select_mentor/send_mentor_request?teacherID=${selectedTeacherID}&studentUID=${studentUID}`);
    if (!response.ok) {
      throw new Error(`Failed to send mentor request: ${response.statusText}`);
    }

    alert("Mentor request sent successfully!");
    backToSelection();
  } catch (error) {
    console.error("Error sending mentor request:", error);
    alert("Unable to send mentor request. Please try again.");
  }
});

// Go back to mentor selection page
backToSelectionBtn.addEventListener('click', backToSelection);

function backToSelection() {
  mentorDetailsPage.classList.add('hidden');
  mentorSelectionPage.classList.remove('hidden');
  teacherDetailsContainer.innerHTML = ''; // Clear previous teacher details
  teachersList.classList.add('hidden'); // Hide teacher list
}

// Helper function to display teacher details
function displayTeacherDetails(details) {
  teacherDetailsContainer.innerHTML = `
    <p><strong>Name:</strong> ${details.firstName} ${details.lastName}</p>
    <p><strong>Qualification:</strong> ${details.qualification}</p>
    <p><strong>Subject Specialization:</strong> ${details.subjectSpecialization}</p>
    <p><strong>Years of Experience:</strong> ${details.yearsExperience}</p>
    <p><strong>Email:</strong> ${details.email}</p>
  `;
}
