import { fetchUserPoints } from '../app';

describe('fetchUserPoints()', () => {
    let doc, el;

    beforeEach(() => {
        // single element stub
        el = { innerText: '' };

        // getElementById always returns the same 'el'
        doc = {
            getElementById: jest.fn().mockReturnValue(el)
        };
    });

    it('updates points on success', async () => {
        const sample = { points: 42 };
        const mockFetch = jest.fn(() =>
            Promise.resolve({ ok: true, json: () => Promise.resolve(sample) })
        );

        await fetchUserPoints('uid123', mockFetch, doc);

        // now el.innerText has been set by fetchUserPoints
        expect(el.innerText).toBe(42);
    });

    it('falls back on error', async () => {
        const mockFetch = jest.fn(() => Promise.resolve({ ok: false }));
        await fetchUserPoints('uid123', mockFetch, doc);

        expect(el.innerText).toBe('0');
    });
});
