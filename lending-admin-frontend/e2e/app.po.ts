import { browser, by, element } from 'protractor';

export class LendingAdminClientPage {
  navigateTo() {
    return browser.get('/');
  }

  getParagraphText() {
    return element(by.css('lending-root h1')).getText();
  }
}
