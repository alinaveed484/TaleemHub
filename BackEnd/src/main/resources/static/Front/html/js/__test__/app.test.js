import { initApp } from '../app';

describe('initApp()', () => {
    let mockFirebase, mockWindow;

    beforeEach(() => {
        mockWindow = { location: { href: '' } };
        mockFirebase = {
            auth: () => ({
                onAuthStateChanged: (cb) => cb(null)
            })
        };
        console.log = jest.fn();
    });

    it('redirects when user is null', () => {
        initApp(mockFirebase, mockWindow);
        expect(console.log).toHaveBeenCalledWith('User not logged in.');
        expect(mockWindow.location.href).toBe('auth-login-basic.html');
    });

    it('logs in when user is present', () => {
        const user = { uid: '123' };
        mockFirebase.auth = () => ({ onAuthStateChanged: cb => cb(user) });

        initApp(mockFirebase, mockWindow);
        expect(console.log).toHaveBeenCalledWith('User logged in:', user);
        expect(mockWindow.location.href).toBe('');
    });
});