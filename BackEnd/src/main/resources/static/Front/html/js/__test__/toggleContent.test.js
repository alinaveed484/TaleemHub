import { toggleContent } from '../app';

describe('toggleContent()', () => {
    const postId = '123';
    let doc, content, button, reply, postComment;

    beforeEach(() => {
        content     = { style: { display: 'none' } };
        button      = { innerText: '' };
        reply       = { style: { display: 'none' } };
        postComment = { style: { display: 'none' } };

        doc = {
            getElementById: jest.fn(id => {
                switch (id) {
                    case `postContent-${postId}`:     return content;
                    case `buttonContent-${postId}`:   return button;
                    case `postReply-${postId}`:       return reply;
                    case `postComment-${postId}`:     return postComment;
                }
            })
        };
    });

    it('shows all parts when hidden', () => {
        toggleContent(postId, doc);
        expect(content.style.display).toBe('block');
        expect(reply.style.display).toBe('block');
        expect(postComment.style.display).toBe('block');
        expect(button.innerText).toBe('Show Less');
    });

    it('hides all parts when visible', () => {
        content.style.display = 'block';
        reply.style.display   = 'block';
        postComment.style.display = 'block';

        toggleContent(postId, doc);
        expect(content.style.display).toBe('none');
        expect(reply.style.display).toBe('none');
        expect(postComment.style.display).toBe('none');
        expect(button.innerText).toBe('Read More');
    });
});
