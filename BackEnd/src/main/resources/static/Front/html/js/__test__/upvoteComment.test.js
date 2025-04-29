import { upvoteComment } from '../app';

describe('upvoteComment()', () => {
    const commentId = 'c1';
    let fetchMock, windowMock;

    beforeEach(() => {
        fetchMock  = jest.fn();
        windowMock = { location: { reload: jest.fn() } };
        console.log   = jest.fn();
        console.error = jest.fn();
    });

    it('reloads on successful upvote', async () => {
        fetchMock.mockResolvedValue({ ok: true, json: () => Promise.resolve({ votes: 10 }) });
        const result = await upvoteComment(commentId, fetchMock, windowMock, console);
        expect(console.log).toHaveBeenCalledWith('Upvote successful:', { votes: 10 });
        expect(windowMock.location.reload).toHaveBeenCalled();
        expect(result).toBe('upvoted');
    });

    it('logs error on failure', async () => {
        fetchMock.mockRejectedValue(new Error('nope'));
        const result = await upvoteComment(commentId, fetchMock, windowMock, console);
        expect(console.error).toHaveBeenCalledWith('Error:', expect.any(Error));
        expect(result).toBe('error');
    });
});
