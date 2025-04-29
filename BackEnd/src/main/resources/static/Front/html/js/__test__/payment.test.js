import { processCardNumber } from '../app';

describe('processCardNumber()', () => {
    let inputEl, mockFetch, mockWindow;
    const fakeUid = 'u1';

    beforeEach(() => {
        inputEl = { value: '1234567890123456' };
        mockFetch = jest.fn(() => Promise.resolve());
        mockWindow = { location: { reload: jest.fn() } };
    });

    it('rejects invalid length', async () => {
        inputEl.value = '1234';
        const result = await processCardNumber(inputEl, { auth: () => ({ onAuthStateChanged: cb => cb({ uid: fakeUid }) }) }, mockFetch, document, mockWindow);
        expect(result).toBe('invalid-length');
    });

    it('stores card and reloads on success', async () => {
        document.body.innerHTML = '<span id="last-four-digits"></span>';
        const result = await processCardNumber(inputEl, { auth: () => ({ onAuthStateChanged: cb => cb({ uid: fakeUid }) }) }, mockFetch, document, mockWindow);

        expect(document.getElementById('last-four-digits').textContent).toBe('3456');
        expect(mockFetch).toHaveBeenCalledWith(`/payments/storeCard?uid=${fakeUid}&cardNumber=1234567890123456`);
        expect(mockWindow.location.reload).toHaveBeenCalled();
        expect(result).toBe('success');
    });
});