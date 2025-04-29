import { sendComment } from '../app';

describe('sendComment()', () => {
    const postId = 'abc';
    let fetchMock, windowMock, alertMock, doc, currentUser;

    beforeEach(() => {
        currentUser = { uid: 'u1' };
        fetchMock   = jest.fn();
        windowMock  = { location: { reload: jest.fn() } };
        alertMock   = jest.fn();
        console.error = jest.fn();

        // default: textarea with empty value
        doc = {
            getElementById: jest.fn(id => {
                if (id === `commentTextArea-${postId}`) {
                    return { value: '' };
                }
            })
        };
    });

    it('alerts if textarea is empty', async () => {
        const result = await sendComment(postId, currentUser, fetchMock, doc, windowMock, alertMock);
        expect(alertMock).toHaveBeenCalledWith('Please enter a comment before sending.');
        expect(fetchMock).not.toHaveBeenCalled();
        expect(result).toBe('empty');
    });

    it('sends fetch and reloads on success', async () => {
        const textArea = { value: 'Hello' };
        doc.getElementById = jest.fn(id => textArea);

        fetchMock.mockResolvedValue({
            ok:   true,
            json: () => Promise.resolve({ message: 'ok' })
        });

        const result = await sendComment(postId, currentUser, fetchMock, doc, windowMock, alertMock);
        expect(fetchMock).toHaveBeenCalledWith(
            `/api/forum/posts/${postId}/comments`,
            expect.objectContaining({
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ content: 'Hello', uid: 'u1' })
            })
        );
        expect(alertMock).toHaveBeenCalledWith('Comment successfully sent!');
        expect(textArea.value).toBe('');
        expect(windowMock.location.reload).toHaveBeenCalled();
        expect(result).toBe('success');
    });

    it('alerts on server or network error', async () => {
        const textArea = { value: 'X' };
        doc.getElementById = jest.fn(id => textArea);

        // simulate non-ok
        fetchMock.mockResolvedValue({ ok: false });
        let result = await sendComment(postId, currentUser, fetchMock, doc, windowMock, alertMock);
        expect(alertMock).toHaveBeenCalledWith('An error occurred while sending the comment.');
        expect(result).toBe('error');

        // simulate network reject
        fetchMock.mockRejectedValue(new Error('fail'));
        alertMock.mockClear();
        result = await sendComment(postId, currentUser, fetchMock, doc, windowMock, alertMock);
        expect(alertMock).toHaveBeenCalledWith('An error occurred while sending the comment.');
        expect(result).toBe('error');
    });
});
