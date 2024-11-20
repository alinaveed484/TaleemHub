// FirebaseUI config.
var uiConfig = {
    signInFlow: 'popup',
    signInSuccessUrl: 'http://localhost:8080/student/select_mentor/display_teachers?subject=Mathematics',
    signInOptions: [
        // Leave the lines as is for the providers you want to offer your users.
        {
            provider: firebase.auth.GoogleAuthProvider.PROVIDER_ID,
            clientId: '61530836051-aq64cqj1gd9ahcdsh7oa3aq1klvb62q8.apps.googleusercontent.com',
        },
        firebase.auth.EmailAuthProvider.PROVIDER_ID,
    ],
    // tosUrl and privacyPolicyUrl accept either url string or a callback
    // function.
    // Terms of service url/callback.
    tosUrl: 'google.com',
    // Privacy policy url/callback.
    privacyPolicyUrl: function() {
        window.location.assign('<your-privacy-policy-url>');
    }
};

// Initialize the FirebaseUI Widget using Firebase.
var ui = new firebaseui.auth.AuthUI(firebase.auth());
// The start method will wait until the DOM is loaded.
ui.start('#firebaseui-auth-container', uiConfig);