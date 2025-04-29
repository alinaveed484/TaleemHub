import { initAuthListener, submitPost } from '../app';

describe('Forum scripts', () => {
    let mockFirebase, mockFetch, mockDoc, mockWindow, mockAlert;

    beforeEach(() => {
        jest.resetModules();
        mockFirebase = {
            auth: () => ({
                onAuthStateChanged: cb => cb(null)
            })
        };
        mockFetch   = jest.fn();
        mockAlert   = jest.fn();
        mockWindow  = { location: { href: '' } };
        mockDoc     = {
            getElementById: jest.fn(id => {
                // generic stub: inputs and button all have .value + .addEventListener
                return { value: '', addEventListener: jest.fn() };
            })
        };
        console.log   = jest.fn();
        console.error = jest.fn();
    });

    it('redirects to login when no user on submit', async () => {
        const result = await submitPost(mockFirebase, mockFetch, mockDoc, mockWindow, mockAlert);
        expect(mockWindow.location.href).toBe('auth-login-basic.html');
        expect(result).toBe('redirected');
    });

    it('posts successfully when response.ok', async () => {
        // simulate logged-in user
        mockFirebase = {
            auth: () => ({
                onAuthStateChanged: cb => cb({ uid: 'u1' })
            })
        };
        mockFetch.mockResolvedValue({
            ok:  true,
            json: () => Promise.resolve({ id: 123 })
        });

        mockDoc.getElementById = jest.fn(id => {
            if (id === 'html5-text-input')             return { value: 'Hello' };
            if (id === 'exampleFormControlTextarea1')  return { value: 'World' };
            // postSubmitBtn stub
            return { addEventListener: jest.fn() };
        });

        const result = await submitPost(mockFirebase, mockFetch, mockDoc, mockWindow, mockAlert);
        expect(result).toBe('success');
        expect(mockAlert).toHaveBeenCalledWith('Post created successfully!');
    });

    it('handles server error and redirects', async () => {
        // simulate logged-in user
        mockFirebase = {
            auth: () => ({
                onAuthStateChanged: cb => cb({ uid: 'u1' })
            })
        };
        mockFetch.mockResolvedValue({
            ok:  false,
            json: () => Promise.resolve({ message: 'Bad things' })
        });

        mockDoc.getElementById = jest.fn(id => {
            if (id === 'html5-text-input')             return { value: 'T' };
            if (id === 'exampleFormControlTextarea1')  return { value: 'C' };
            return { addEventListener: jest.fn() };
        });

        const result = await submitPost(mockFirebase, mockFetch, mockDoc, mockWindow, mockAlert);
        expect(result).toBe('error');
        expect(mockWindow.location.href).toBe('/Front/html/auth-login-basic.html');
    });
});
