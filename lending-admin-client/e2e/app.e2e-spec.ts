import { LendingAdminClientPage } from './app.po';

describe('lending-admin-client App', () => {
  let page: LendingAdminClientPage;

  beforeEach(() => {
    page = new LendingAdminClientPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to lending!');
  });
});
