import { logoutHandler } from '../app';

describe('logoutHandler', () => {
    let mockFirebase, mockWindow, link;

    beforeEach(() => {
        // 1) Stub out the element and its addEventListener
        link = { addEventListener: jest.fn() };
        document.getElementById = jest.fn().mockReturnValue(link);

        // 2) Default: signOut resolves
        mockFirebase = {
            auth: () => ({
                signOut: () => Promise.resolve()
            })
        };
        mockWindow  = { location: { href: '' } };

        // 3) Spy on console
        console.log   = jest.fn();
        console.error = jest.fn();
    });

    it('signs out and redirects', async () => {
        logoutHandler(mockFirebase, mockWindow, console);
        const handler = link.addEventListener.mock.calls[0][1];

        // This now returns a promise that we can await
        await handler({ preventDefault: () => {} });

        expect(console.log).toHaveBeenCalledWith('User logged out successfully.');
        expect(mockWindow.location.href).toBe('auth-login-basic.html');
    });

    it('logs error on signOut failure', async () => {
        // 1) Make signOut reject
        mockFirebase = {
            auth: () => ({
                signOut: () => Promise.reject(new Error('fail'))
            })
        };
        link.addEventListener.mockClear();

        logoutHandler(mockFirebase, mockWindow, console);
        const handler = link.addEventListener.mock.calls[0][1];

        // Await the handler so the catch block has run
        await handler({ preventDefault: () => {} });

        expect(console.error).toHaveBeenCalledWith('Error logging out:', 'fail');
    });
});
