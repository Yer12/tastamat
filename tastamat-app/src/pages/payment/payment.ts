import { Component } from '@angular/core';
import { IonicPage, ViewController } from 'ionic-angular';
import { InAppBrowser, InAppBrowserOptions } from '@ionic-native/in-app-browser';
import { ProfileService } from "../../services/profile.service";

@IonicPage()
@Component({
  selector: 'page-payment',
  templateUrl: 'payment.html',
})
export class PaymentPage {
  amount: number;

  constructor(private view: ViewController, private iab: InAppBrowser, private profileService: ProfileService) {}

  closeModal() {
    this.view.dismiss();
  }

  createPayment() {
    this.profileService.fillUpWallet(this.amount).subscribe(
      res => this.openBrowser(res.url),
      err => console.log(err)
    )
  }

  openBrowser(url) {
    this.closeModal();
    const options: InAppBrowserOptions = {
      zoom: 'no',
      hardwareback: 'no',
      location: 'no'
    };
    const browser = this.iab.create(url, '_blank', options);
    browser.on('loadstart').subscribe(event => {
      if (event.url === 'https://tastamat.kz/') {
        browser.close();
      }
    });
  }

}
