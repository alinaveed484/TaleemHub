import { connectHandler } from '../app';

describe('connectHandler', () => {
    let mockFirebase, mockFetch, mockWindow, mockAlert, link;

    beforeEach(() => {
        // stub your link
        link = { addEventListener: jest.fn() };

        // override jsdom document.getElementById
        document.getElementById = jest.fn().mockReturnValue(link);

        mockFetch  = jest.fn();
        mockWindow = { location: { href: '' } };
        mockAlert  = jest.fn();

        console.log   = jest.fn();
        console.error = jest.fn();

        // by default simulate a logged-in user
        mockFirebase = {
            auth: () => ({
                onAuthStateChanged: cb => cb({ uid: 'u1' })
            })
        };
    });

    it('follows redirect when response.redirected', async () => {
        mockFetch.mockResolvedValue({ redirected: true, url: '/go', ok: true });
        connectHandler(mockFirebase, mockFetch, mockWindow, console, mockAlert);

        const handler = link.addEventListener.mock.calls[0][1];
        await handler({ preventDefault: () => {} });

        expect(mockWindow.location.href).toBe('/go');
    });

    it('alerts if no user', async () => {
        // simulate no user
        mockFirebase = {
            auth: () => ({
                onAuthStateChanged: cb => cb(null)
            })
        };
        connectHandler(mockFirebase, mockFetch, mockWindow, console, mockAlert);

        const handler = link.addEventListener.mock.calls[0][1];
        await handler({ preventDefault: () => {} });

        expect(mockAlert).toHaveBeenCalledWith('You must log in first.');
    });
});
