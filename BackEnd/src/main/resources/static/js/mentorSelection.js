'use strict';

const mentorSelectionPage = document.querySelector('#mentor-selection-page');
const mentorDetailsPage = document.querySelector('#mentor-details-page');
const mentorSelectionForm = document.querySelector('#mentorSelectionForm');
const showTeacherDetailsBtn = document.querySelector('#showTeacherDetailsBtn');
const sendMentorRequestBtn = document.querySelector('#sendMentorRequestBtn');
const backToSelectionBtn = document.querySelector('#backToSelectionBtn');
const teacherDetailsContainer = document.querySelector('#teacherDetails');

let selectedTeacherID = null;
let studentID = null;

// Fetch and display teacher details
showTeacherDetailsBtn.addEventListener('click', async () => {
    const teacherIDInput = document.querySelector('#teacherID').value.trim();
    const studentIDInput = document.querySelector('#studentID').value.trim();

    if (!teacherIDInput || !studentIDInput) {
        alert("Both Student ID and Teacher ID are required!");
        return;
    }

    selectedTeacherID = parseInt(teacherIDInput);
    studentID = parseInt(studentIDInput);

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
});

// Send mentor request
sendMentorRequestBtn.addEventListener('click', async () => {
    if (!selectedTeacherID || !studentID) {
        alert("Teacher ID and Student ID are required to send a mentor request!");
        return;
    }

    try {
        const response = await fetch(`/student/select_mentor/send_mentor_request?teacherID=${selectedTeacherID}&studentID=${studentID}`);
        if (!response.ok) {
            throw new Error(`Failed to send mentor request: ${response.statusText}`);
        }

        const result = await response.json();
        alert("Mentor request sent successfully!");
        console.log("Mentor request response:", result);
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
}

// Helper function to display teacher details
function displayTeacherDetails(details) {
    teacherDetailsContainer.innerHTML = `
        <p><strong>Teacher ID:</strong> ${details.teacherId}</p>
        <p><strong>Name:</strong> ${details.firstName} ${details.lastName}</p>
        <p><strong>Qualification:</strong> ${details.qualification}</p>
        <p><strong>Subject Specialization:</strong> ${details.subjectSpecialization}</p>
        <p><strong>Years of Experience:</strong> ${details.yearsExperience}</p>
        <p><strong>Email:</strong> ${details.email}</p>
    `;
}
