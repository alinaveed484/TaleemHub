// Import the functions you need from the SDKs you need
//import { initializeApp } from "https://www.gstatic.com/firebasejs/11.0.2/firebase-app.js";
//import { getAnalytics } from "https://www.gstatic.com/firebasejs/11.0.2/firebase-analytics.js";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyAkOsZUplCSLcp84Oqr_SqrGtm5I-TfP3g",
    authDomain: "taleemhub-6d11d.firebaseapp.com",
    projectId: "taleemhub-6d11d",
    storageBucket: "taleemhub-6d11d.firebasestorage.app",
    messagingSenderId: "61530836051",
    appId: "1:61530836051:web:66a1bead55044e1824caa6",
    measurementId: "G-KZJHK025KR"
};

// Initialize Firebase
const app = firebase.initializeApp(firebaseConfig);
const analytics = firebase.analytics(app);