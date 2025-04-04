const apiUrl = 'http://localhost:5000';

function showLogin() {
    document.getElementById('login-username').value = '';
    document.getElementById('login-password').value = '';
    document.getElementById('register-username').value = '';
    document.getElementById('register-password').value = '';
    document.getElementById('auth-container').classList.remove('hidden');
    document.getElementById('login-form').classList.remove('hidden');
    document.getElementById('register-form').classList.add('hidden');
}

async function checkToken() {
    const token = getAuthToken();

    hideAuth();
    hidePostsPage();

    if (token) {
        const response = await fetch(`${apiUrl}/tokencheck`, {
            method: 'GET',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            hideAuth();
            showPostsPage();
            return;
        } else {
            hidePostsPage();
            showLogin();
            return;
        }
    } else {
        hidePostsPage();
        showLogin();
        return;
    }
}

function hideAuth() {
    document.getElementById('auth-container').classList.add('hidden');
    document.getElementById('register-form').classList.add('hidden');
    document.getElementById('login-form').classList.add('hidden');
}

function showRegister() {
    document.getElementById('register-form').classList.remove('hidden');
    document.getElementById('login-form').classList.add('hidden');
}

function showPostsPage() {
    document.getElementById('auth-container').classList.add('hidden');
    document.getElementById('posts-page').classList.remove('hidden');
    fetchPosts();
}

function hidePostsPage() {
    document.getElementById('posts-page').classList.add('hidden');
}

async function login() {
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;

    if(!username && !password) {
        alert('Bejelentkezéshez név és jelszó megadása kötelező!');
        return;
    }

    const response = await fetch(`${apiUrl}/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
    });

    if (response.ok) {
        const data = await response.json();
        document.cookie = `auth_token=${data.token}; path=/; max-age=3600`;
        hideAuth();
        showPostsPage();
    } else {
        const data = await response.json();
        alert(data.message);
    }
}

async function register() {
    const username = document.getElementById('register-username').value;
    const password = document.getElementById('register-password').value;

    if(!username && !password) {
        alert('Regisztrációhoz név és jelszó megadása kötelező!');
        return;
    }

    const response = await fetch(`${apiUrl}/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
    });

    if (response.ok) {
        alert('Registration successful!');
        showLogin();
    } else {
        const data = await response.json();
        alert(data.message);
    }
}

function logout() {
    document.cookie = 'auth_token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC';
    hidePostsPage();
    showLogin();    
}

async function fetchPosts() {
    const token = getAuthToken();

    const response = await fetch(`${apiUrl}/posts`, {
        method: 'GET',
        headers: { 'Authorization': `Bearer ${token}` }
    });

    const posts = await response.json();
    displayPosts(posts);
}

async function updatePost(postId, updatedTitle, updatedContent) {
    const token = getAuthToken();

    const response = await fetch(`${apiUrl}/post/${postId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ title: updatedTitle, content: updatedContent })
    });

    if (response.ok) {
        fetchPosts();
    } else {
        const data = await response.json();
        alert(data.message);
    }
}

function displayPosts(posts) {
    const postsList = document.getElementById('posts-list');
    postsList.innerHTML = '';

    posts.forEach(post => {
        const postDiv = document.createElement('div');
        postDiv.classList.add('post');
        postDiv.innerHTML = `
            <h3>${post.title}</h3>
            <p>${post.content}</p>
            <button class="delete-post" onclick="deletePost('${post._id}')">Törlés 🗑️</button>
            <button class="edit-post" onclick="updatePost('${post._id}', prompt('Új cim:', '${post.title}'), prompt('Új szövegtörzs:', '${post.content}'))">Szerkesztés ✏️</button>
        `;
        postsList.appendChild(postDiv);
    });
}

document.getElementById('post-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const title = document.getElementById('post-title').value;
    const content = document.getElementById('post-content').value;
    const token = getAuthToken();

    const response = await fetch(`${apiUrl}/post`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ title, content })
    });

    if (response.ok) {
        fetchPosts();
    } else {
        const data = await response.json();
        alert(data.message);
    }
});

async function deletePost(postId) {
    const token = getAuthToken();
    console.log(token);
    const response = await fetch(`${apiUrl}/post/${postId}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
    });

    if (response.ok) {
        fetchPosts();
    } else {
        const data = await response.json();
        alert(data.message);
    }
}

function getAuthToken() {
    const cookies = document.cookie.split('; ');
    const tokenCookie = cookies.find(cookie => cookie.startsWith('auth_token='));
    return tokenCookie ? tokenCookie.split('=')[1] : null;
}

checkToken();
