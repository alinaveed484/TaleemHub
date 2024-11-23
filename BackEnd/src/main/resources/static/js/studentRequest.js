//'use strict';
//
//const teacherRequestForm = document.querySelector('#teacherRequestForm');
//const showRequestsBtn = document.querySelector('#showRequestsBtn');
//const studentRequestsPage = document.querySelector('#student-requests-page');
//const studentRequestsList = document.querySelector('#studentRequestsList');
//const studentDetailsPage = document.querySelector('#student-details-page');
//const studentDetailsContainer = document.querySelector('#studentDetails');
//const acceptRequestBtn = document.querySelector('#acceptRequestBtn');
//const backToRequestsBtn = document.querySelector('#backToRequestsBtn');
//const backToTeacherPageBtn = document.querySelector('#backToTeacherPageBtn');
//
//let selectedStudentID = null;
//let teacherID = null;
//
//// Fetch and display all student requests for the teacher
//showRequestsBtn.addEventListener('click', async () => {
//    const teacherIDInput = document.querySelector('#teacherID').value.trim();
//    if (!teacherIDInput) {
//        alert("Please enter your Teacher ID!");
//        return;
//    }
//
//    teacherID = parseInt(teacherIDInput);
//
//    try {
//        const response = await fetch(`/teacher/accept_student/display_students?teacherId=${teacherID}`);
//        if (!response.ok) {
//            throw new Error(`Failed to fetch student requests: ${response.statusText}`);
//        }
//
//        const studentRequests = await response.json();
//        displayStudentRequests(studentRequests);
//    } catch (error) {
//        console.error("Error fetching student requests:", error);
//        alert("Unable to fetch student requests. Please try again.");
//    }
//});
//
//// Display list of student requests
//function displayStudentRequests(requests) {
//    studentRequestsList.innerHTML = ''; // Clear previous list
//    if (Object.keys(requests).length === 0) {
//        studentRequestsList.innerHTML = '<li>No student requests found.</li>';
//        return;
//    }
//
//    Object.entries(requests).forEach(([studentID, details]) => {
//        const listItem = document.createElement('li');
//        listItem.textContent = `${details.firstName} ${details.lastName} - ${details.type}`;
//        listItem.dataset.studentId = studentID; // Store student ID for later reference
//        console.log(`Setting student ID: ${studentID}`); // Debugging log
//        listItem.addEventListener('click', () => {
//            console.log(`Clicked student ID: ${studentID}`); // Debugging log
//            fetchStudentDetails(studentID);
//        });
//        studentRequestsList.appendChild(listItem);
//    });
//
//    studentRequestsPage.querySelector('.student-requests').classList.remove('hidden');
//    backToTeacherPageBtn.classList.remove('hidden');
//}
//
//
//// Fetch and display detailed information for a specific student
//async function fetchStudentDetails(studentID) {
//    console.log(`Fetching details for student ID: ${studentID}`); // Debugging log
//    try {
//        const response = await fetch(`/teacher/accept_student/show_student_request?teacherID=${teacherID}&studentID=${studentID}`);
//        if (!response.ok) {
//            throw new Error(`Failed to fetch student details: ${response.statusText}`);
//        }
//
//        const studentDetails = await response.json();
//        console.log(`Fetched student details:`, studentDetails); // Debugging log
//        displayStudentDetails(studentDetails, studentID);
//    } catch (error) {
//        console.error("Error fetching student details:", error);
//        alert("Unable to fetch student details. Please try again.");
//    }
//}
//
//
//
//// Display student details and show accept button
//function displayStudentRequests(requests) {
//    const students = requests.students; // Access the "students" key
//    console.log("Student Requests Data:", students); // Log for debugging
//
//    studentRequestsList.innerHTML = ''; // Clear previous list
//    if (!students || Object.keys(students).length === 0) {
//        studentRequestsList.innerHTML = '<li>No student requests found.</li>';
//        return;
//    }
//
//    Object.entries(students).forEach(([studentID, details]) => {
//        console.log(`Student ID: ${studentID}, Details:`, details); // Debug log
//
//        const listItem = document.createElement('li');
//        listItem.textContent = `${details.firstName} ${details.lastName} - ${details.type}`;
//        listItem.dataset.studentId = details.id; // Use the "id" field from the details
//        listItem.addEventListener('click', () => {
//            console.log(`Clicked student ID: ${details.id}`); // Debug log
//            fetchStudentDetails(details.id); // Pass the correct student ID
//        });
//        studentRequestsList.appendChild(listItem);
//    });
//
//    studentRequestsPage.querySelector('.student-requests').classList.remove('hidden');
//    backToTeacherPageBtn.classList.remove('hidden');
//}
//
//
//// Accept student request
//acceptRequestBtn.addEventListener('click', async () => {
//    if (!teacherID || !selectedStudentID) {
//        alert("Missing Teacher ID or Student ID to accept the request!");
//        return;
//    }
//
//    try {
//        const response = await fetch(`/teacher/accept_student/accept_student?teacherID=${teacherID}&studentID=${selectedStudentID}`);
//        if (!response.ok) {
//            throw new Error(`Failed to accept student: ${response.statusText}`);
//        }
//
//        alert("Student request accepted successfully!");
//        backToRequests();
//    } catch (error) {
//        console.error("Error accepting student request:", error);
//        alert("Unable to accept student request. Please try again.");
//    }
//});
//
//// Go back to student requests page
//backToRequestsBtn.addEventListener('click', backToRequests);
//backToTeacherPageBtn.addEventListener('click', () => {
//    studentRequestsList.innerHTML = '';
//    studentRequestsPage.querySelector('.student-requests').classList.add('hidden');
//    backToTeacherPageBtn.classList.add('hidden');
//});
//
//function backToRequests() {
//    studentDetailsPage.classList.add('hidden');
//    studentRequestsPage.classList.remove('hidden');
//    studentDetailsContainer.innerHTML = ''; // Clear previous details
//}
//
//function displayStudentDetails(details) {
//    studentDetailsContainer.innerHTML = `
//        <p><strong>Student ID:</strong> ${details.id}</p>
//        <p><strong>Name:</strong> ${details.firstName} ${details.lastName}</p>
//        <p><strong>Grade:</strong> ${details.grade}</p>
//        <p><strong>Subjects:</strong> ${details.subjects}</p>
//        <p><strong>Email:</strong> ${details.email}</p>
//    `;
//}

//
//'use strict';
//
//const teacherRequestForm = document.querySelector('#teacherRequestForm');
//const showRequestsBtn = document.querySelector('#showRequestsBtn');
//const studentRequestsPage = document.querySelector('#student-requests-page');
//const studentRequestsList = document.querySelector('#studentRequestsList');
//const studentDetailsPage = document.querySelector('#student-details-page');
//const studentDetailsContainer = document.querySelector('#studentDetails');
//const acceptRequestBtn = document.querySelector('#acceptRequestBtn');
//const backToRequestsBtn = document.querySelector('#backToRequestsBtn');
//const backToTeacherPageBtn = document.querySelector('#backToTeacherPageBtn');
//
//let selectedStudentID = null;
//let teacherID = null;
//
//// Fetch and display all student requests for the teacher
//showRequestsBtn.addEventListener('click', async () => {
//    const teacherIDInput = document.querySelector('#teacherID').value.trim();
//    if (!teacherIDInput) {
//        alert("Please enter your Teacher ID!");
//        return;
//    }
//
//    teacherID = parseInt(teacherIDInput);
//
//    try {
//        const response = await fetch(`/teacher/accept_student/display_students?teacherId=${teacherID}`);
//        if (!response.ok) {
//            throw new Error(`Failed to fetch student requests: ${response.statusText}`);
//        }
//
//        const studentRequests = await response.json();
//        displayStudentRequests(studentRequests);
//    } catch (error) {
//        console.error("Error fetching student requests:", error);
//        alert("Unable to fetch student requests. Please try again.");
//    }
//});
//
//// Display list of student requests
//function displayStudentRequests(requests) {
//    const students = requests.students; // Access the "students" key
//    studentRequestsList.innerHTML = ''; // Clear previous list
//    if (!students || Object.keys(students).length === 0) {
//        studentRequestsList.innerHTML = '<li>No student requests found.</li>';
//        return;
//    }
//
//    Object.entries(students).forEach(([studentID, details]) => {
//        const listItem = document.createElement('li');
//        listItem.textContent = `${details.firstName} ${details.lastName} - ${details.type}`;
//        listItem.dataset.studentId = details.id; // Use the "id" field from the details
//        listItem.addEventListener('click', () => fetchStudentDetails(details.id)); // Fetch details on click
//        studentRequestsList.appendChild(listItem);
//    });
//
//    studentRequestsPage.querySelector('.student-requests').classList.remove('hidden');
//    backToTeacherPageBtn.classList.remove('hidden');
//}
//
//// Fetch and display detailed information for a specific student
//async function fetchStudentDetails(studentID) {
//    console.log(`Fetching details for student ID: ${studentID}`); // Debugging log
//    try {
//        const response = await fetch(`/teacher/accept_student/show_student_request?teacherID=${teacherID}&studentID=${studentID}`);
//        if (!response.ok) {
//            throw new Error(`Failed to fetch student details: ${response.statusText}`);
//        }
//
//        const studentResponse = await response.json();
//        console.log("Student details received from backend:", studentResponse); // Debugging log
//
//        const studentDetails = studentResponse.student; // Extract the student object
//        displayStudentDetails(studentDetails, studentID);
//    } catch (error) {
//        console.error("Error fetching student details:", error);
//        alert("Unable to fetch student details. Please try again.");
//    }
//}
//
//
//// Display student details and show accept button
//function displayStudentDetails(details, studentID) {
//    console.log("Student details to display:", details); // Debugging log
//    selectedStudentID = studentID;
//
//    // Update the HTML to display the student details
//    studentDetailsContainer.innerHTML = `
//        <p><strong>ID:</strong> ${details.id || "N/A"}</p>
//        <p><strong>Name:</strong> ${details.firstName || "N/A"} ${details.lastName || "N/A"}</p>
//        <p><strong>Status:</strong> ${details.status ? "Online" : "Offline"}</p>
//        <p><strong>Type:</strong> ${details.type || "N/A"}</p>
//    `;
//
//    studentRequestsPage.classList.add('hidden');
//    studentDetailsPage.classList.remove('hidden');
//}
//
//
//// Accept student request
//acceptRequestBtn.addEventListener('click', async () => {
//    if (!teacherID || !selectedStudentID) {
//        alert("Missing Teacher ID or Student ID to accept the request!");
//        return;
//    }
//
//    try {
//        const response = await fetch(`/teacher/accept_student/accept_student?teacherID=${teacherID}&studentID=${selectedStudentID}`);
//        if (!response.ok) {
//            throw new Error(`Failed to accept student: ${response.statusText}`);
//        }
//
//        alert("Student request accepted successfully!");
//        backToRequests();
//    } catch (error) {
//        console.error("Error accepting student request:", error);
//        alert("Unable to accept student request. Please try again.");
//    }
//});
//
//// Go back to student requests page
//backToRequestsBtn.addEventListener('click', backToRequests);
//backToTeacherPageBtn.addEventListener('click', () => {
//    studentRequestsList.innerHTML = '';
//    studentRequestsPage.querySelector('.student-requests').classList.add('hidden');
//    backToTeacherPageBtn.classList.add('hidden');
//});
//
//function backToRequests() {
//    studentDetailsPage.classList.add('hidden');
//    studentRequestsPage.classList.remove('hidden');
//    studentDetailsContainer.innerHTML = ''; // Clear previous details
//}

'use strict';

const studentRequestsPage = document.querySelector('#student-requests-page');
const studentRequestsList = document.querySelector('#studentRequestsList');
const studentDetailsPage = document.querySelector('#student-details-page');
const studentDetailsContainer = document.querySelector('#studentDetails');
const acceptRequestBtn = document.querySelector('#acceptRequestBtn');
const backToRequestsBtn = document.querySelector('#backToRequestsBtn');
const backToTeacherPageBtn = document.querySelector('#backToTeacherPageBtn');

let selectedStudentID = null;
let teacherUID = null;

// Initialize Firebase and retrieve the teacher's UID
firebase.auth().onAuthStateChanged(async (user) => {
    if (user) {
        console.log("Logged in as:", user.uid);
        teacherUID = user.uid; // Assume teacherID corresponds to Firebase UID

        try {
            // Fetch and display student requests
            const response = await fetch(`/teacher/accept_student/display_students?teacherUID=${teacherUID}`);
            if (!response.ok) {
                throw new Error(`Failed to fetch student requests: ${response.statusText}`);
            }

            const studentRequests = await response.json();
            displayStudentRequests(studentRequests);
        } catch (error) {
            console.error("Error fetching student requests:", error);
            alert("Unable to fetch student requests. Please try again.");
        }
    } else {
        console.log("No teacher is logged in.");
        alert("You must be logged in as a teacher to view this page.");
        // Optionally redirect to login page
    }
});

// Display list of student requests
function displayStudentRequests(requests) {
    const students = requests.students; // Access the "students" key
    studentRequestsList.innerHTML = ''; // Clear previous list
    if (!students || Object.keys(students).length === 0) {
        studentRequestsList.innerHTML = '<li>No student requests found.</li>';
        return;
    }

    Object.entries(students).forEach(([studentID, details]) => {
        const listItem = document.createElement('li');
        listItem.textContent = `${details.firstName} ${details.lastName} - ${details.type}`;
        listItem.dataset.studentId = details.id; // Use the "id" field from the details
        listItem.addEventListener('click', () => fetchStudentDetails(details.id)); // Fetch details on click
        studentRequestsList.appendChild(listItem);
    });

    studentRequestsPage.querySelector('.student-requests').classList.remove('hidden');
    backToTeacherPageBtn.classList.remove('hidden');
}

// Fetch and display detailed information for a specific student
async function fetchStudentDetails(studentID) {
    try {
        const response = await fetch(`/teacher/accept_student/show_student_request?teacherUID=${teacherUID}&studentID=${studentID}`);
        if (!response.ok) {
            throw new Error(`Failed to fetch student details: ${response.statusText}`);
        }

        const studentResponse = await response.json();
        const studentDetails = studentResponse.student; // Extract the student object
        displayStudentDetails(studentDetails, studentID);
    } catch (error) {
        console.error("Error fetching student details:", error);
        alert("Unable to fetch student details. Please try again.");
    }
}

// Display student details and show accept button
function displayStudentDetails(details, studentID) {
    selectedStudentID = studentID;

    studentDetailsContainer.innerHTML = `
        <p><strong>ID:</strong> ${details.id || "N/A"}</p>
        <p><strong>Name:</strong> ${details.firstName || "N/A"} ${details.lastName || "N/A"}</p>
        <p><strong>Status:</strong> ${details.status ? "Online" : "Offline"}</p>
        <p><strong>Type:</strong> ${details.type || "N/A"}</p>
    `;

    studentRequestsPage.classList.add('hidden');
    studentDetailsPage.classList.remove('hidden');
}

// Accept student request
acceptRequestBtn.addEventListener('click', async () => {
    if (!teacherUID || !selectedStudentID) {
        alert("Missing Teacher ID or Student ID to accept the request!");
        return;
    }

    try {
        const response = await fetch(`/teacher/accept_student/accept_student?teacherUID=${teacherUID}&studentID=${selectedStudentID}`);
        if (!response.ok) {
            throw new Error(`Failed to accept student: ${response.statusText}`);
        }

        alert("Student request accepted successfully!");
        backToRequests();
    } catch (error) {
        console.error("Error accepting student request:", error);
        alert("Unable to accept student request. Please try again.");
    }
});

// Go back to student requests page
backToRequestsBtn.addEventListener('click', backToRequests);
backToTeacherPageBtn.addEventListener('click', () => {
    studentRequestsList.innerHTML = '';
    studentRequestsPage.querySelector('.student-requests').classList.add('hidden');
    backToTeacherPageBtn.classList.add('hidden');
});

function backToRequests() {
    studentDetailsPage.classList.add('hidden');
    studentRequestsPage.classList.remove('hidden');
    studentDetailsContainer.innerHTML = ''; // Clear previous details
}
