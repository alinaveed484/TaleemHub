const API_BASE_URL = 'http://localhost:8080';

const API = {
  get: async (url) => {
    const response = await fetch(`${API_BASE_URL}${url}`);
    return response.json();
  },
  post: async (url, data) => {
    const response = await fetch(`${API_BASE_URL}${url}`, {
      method: 'POST', // Ensure POST is specified here
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    return response.json();
  }
};

export default API;
