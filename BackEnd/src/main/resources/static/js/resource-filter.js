function filterResources() {
    const subject = document.getElementById("subject").value;
    if (subject === "") {
        window.location.href = "/resources/view";
    } else {
        window.location.href = `/resources/view?subject=${subject}`;
    }
}

// Set the dropdown value dynamically based on URL parameters
window.onload = function () {
    const urlParams = new URLSearchParams(window.location.search);
    const subject = urlParams.get("subject");
    if (subject) {
        document.getElementById("subject").value = subject;
    }
};