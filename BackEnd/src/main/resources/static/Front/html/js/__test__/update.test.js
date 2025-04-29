import { updateUserName } from '../app';

describe('updateUserName()', () => {
    beforeEach(() => {
        document.body.innerHTML = `
      <span id="personName1"></span>
      <span id="personName2"></span>
    `;
    });

    it('sets both name fields', () => {
        updateUserName('Alice', document);
        expect(document.getElementById('personName1').textContent).toBe('Alice');
        expect(document.getElementById('personName2').textContent).toBe('Welcome Alice');
    });
});