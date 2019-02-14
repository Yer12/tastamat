import { Component } from '@angular/core';
import { IonicPage, ViewController } from 'ionic-angular';
import { InAppBrowser, InAppBrowserOptions } from '@ionic-native/in-app-browser/ngx';

@IonicPage()
@Component({
  selector: 'page-payment',
  templateUrl: 'payment.html',
})
export class PaymentPage {
  amount: string;

  constructor(private view: ViewController, private iab: InAppBrowser) {}

  closeModal() {
    this.view.dismiss();
  }

  openBrowser() {
    const options: InAppBrowserOptions = {
      zoom: 'no',
      hardwareback: 'no',
      location: 'yes'
    };
    const browser = this.iab.create('https://ionicframework.com/', '_blank', options);
    browser.on('loadstart').subscribe(event => {
      if (event.url === 'https://ionicframework.com/appflow') {
        browser.close();
        this.closeModal();
      }
    });
  }

}
