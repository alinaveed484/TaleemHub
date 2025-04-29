export function initApp(firebase, window) {
    firebase.auth().onAuthStateChanged(user => {
        if (user) {
            console.log('User logged in:', user);
        } else {
            console.log('User not logged in.');
            window.location.href = 'auth-login-basic.html';
        }
    });
}

export function fetchUserPoints(uid, fetch, document) {
    return fetch(`/persons/${uid}/points`)
        .then(r => { if (!r.ok) throw new Error('Failed'); return r.json(); })
        .then(data => { document.getElementById('user-points').innerText = data.points; })
        .catch(() => { document.getElementById('user-points').innerText = '0'; });
}

export function processCardNumber(inputEl, firebase, fetch, document, window) {
    const card = inputEl.value.trim();
    return new Promise(resolve => {
        firebase.auth().onAuthStateChanged(async user => {
            if (!user) return resolve('not-logged-in');

            if (card.length !== 16) {
                return resolve('invalid-length');
            }

            document.getElementById('last-four-digits').textContent = card.slice(-4);
            await fetch(`/payments/storeCard?uid=${user.uid}&cardNumber=${card}`);
            window.location.reload();
            resolve('success');
        });
    });
}

export function updateUserName(name, document) {
    document.getElementById('personName1').textContent = name;
    document.getElementById('personName2').textContent = 'Welcome ' + name;
}

//  ====================================================>> postInput.html test cases
// Holds the current user once onAuthStateChanged fires
let currentUser = null;

// 1) AUTH LISTENER
export function initAuthListener(firebase, console) {
    firebase.auth().onAuthStateChanged(user => {
        if (user) {
            console.log('User logged in:', user);
            currentUser = user;
        } else {
            console.log('User not logged in.');
            currentUser = null;
        }
    });
}

/**
 * Simulates the click‐handler for posting to the forum.
 * @param {object} firebase  – the firebase SDK (so tests can inject a fake)
 * @param {Function} fetch
 * @param {Document} document
 * @param {Window} window
 * @param {Function} alert
 * @returns {Promise<'redirected'|'success'|'error'|'network-error'>}
 */
export function submitPost(firebase, fetch, document, window, alert) {
    return new Promise(resolve => {
        // mirror your original click callback
        firebase.auth().onAuthStateChanged(async user => {
            // 1) not logged in?
            if (!user) {
                console.log('User not logged in.');
                window.location.href = 'auth-login-basic.html';
                return resolve('redirected');
            }

            // 2) collect form values
            const title   = document.getElementById('html5-text-input').value;
            const content = document.getElementById('exampleFormControlTextarea1').value;
            const dto     = { title, content, uid: user.uid };

            // 3) post to backend
            try {
                const res = await fetch('/api/forum/posts', {
                    method:  'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body:    JSON.stringify(dto)
                });

                if (res.ok) {
                    const data = await res.json();
                    console.log('Post created successfully:', data);
                    alert('Post created successfully!');
                    return resolve('success');
                } else {
                    const err = await res.json();
                    console.error('Error creating post:', err);
                    alert('Error creating post: ' + (err.message || 'Unknown error'));
                    window.location.href = '/Front/html/auth-login-basic.html';
                    return resolve('error');
                }
            } catch (e) {
                console.error('Network error:', e);
                return resolve('network-error');
            }
        });
    });
}

// 3) LOGOUT HANDLER

export function logoutHandler(firebase, window, console) {
    const link = document.getElementById('logout-link');
    link.addEventListener('click', async (e) => {
        e.preventDefault();
        try {
            await firebase.auth().signOut();
            console.log('User logged out successfully.');
            window.location.href = 'auth-login-basic.html';
        } catch (err) {
            // return err.message for tests if you want, but logging is enough
            console.error('Error logging out:', err.message);
        }
    });
}

// 4) CONNECT-LINK HANDLER
export function connectHandler(firebase, fetch, window, console, alert) {
    const link = document.getElementById('connectLink');
    link.addEventListener('click', e => {
        e.preventDefault();
        firebase.auth().onAuthStateChanged(async user => {
            if (!user) {
                console.log('User not logged in.');
                alert('You must log in first.');
                return;
            }
            try {
                const res = await fetch(`/connect_check?uid=${user.uid}`, { redirect: 'follow' });
                if (res.redirected) {
                    window.location.href = res.url;
                } else if (!res.ok) {
                    throw new Error('Failed to process request');
                }
            } catch (err) {
                console.error('Error:', err);
            }
        });
    });
}


/**
 * Toggle show/hide of a post’s content, reply box, and comment form.
 */
export function toggleContent(postId, document) {
    const content     = document.getElementById(`postContent-${postId}`);
    const button      = document.getElementById(`buttonContent-${postId}`);
    const reply       = document.getElementById(`postReply-${postId}`);
    const postComment = document.getElementById(`postComment-${postId}`);

    if (content.style.display === 'none') {
        content.style.display     = 'block';
        reply.style.display       = 'block';
        postComment.style.display = 'block';
        button.innerText          = 'Show Less';
    } else {
        content.style.display     = 'none';
        reply.style.display       = 'none';
        postComment.style.display = 'none';
        button.innerText          = 'Read More';
    }
}

/**
 * Send a comment on a post. Returns a Promise so tests can await it.
 */
export function sendComment(postId, currentUser, fetch, document, window, alert) {
    const textArea = document.getElementById(`commentTextArea-${postId}`);
    const comment  = textArea.value.trim();

    if (!comment) {
        alert('Please enter a comment before sending.');
        return Promise.resolve('empty');
    }

    const data = { content: comment, uid: currentUser.uid };

    return fetch(`/api/forum/posts/${postId}/comments`, {
        method:  'POST',
        headers: { 'Content-Type': 'application/json' },
        body:    JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) throw new Error('Failed to send comment');
            return response.json();
        })
        .then(() => {
            alert('Comment successfully sent!');
            textArea.value = '';
            window.location.reload();
            return 'success';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while sending the comment.');
            return 'error';
        });
}

/**
 * Upvote a comment. Returns a Promise so tests can await it.
 */
export function upvoteComment(commentId, fetch, window, console) {
    return fetch(`/api/forum/comments/${commentId}/upvote`, {
        method:  'POST',
        headers: { 'Content-Type': 'application/json' }
    })
        .then(response => {
            if (!response.ok) throw new Error('Failed to upvote comment');
            return response.json();
        })
        .then(data => {
            console.log('Upvote successful:', data);
            window.location.reload();
            return 'upvoted';
        })
        .catch(error => {
            console.error('Error:', error);
            return 'error';
        });
}


