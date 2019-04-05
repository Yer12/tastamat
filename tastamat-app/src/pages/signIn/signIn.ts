import { Component } from '@angular/core';
import { NavController, Platform } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { AlertController } from 'ionic-angular';
import { TranslateService } from "@ngx-translate/core";
import { EnterSmsCodePage } from "../enterSmsCode/enterSmsCode";
import { SmsService } from "../../services/sms.service";
import { AuthService } from "../../services/auth.service";
import { TabsPage } from "../tabs/tabs";
import { InAppBrowser, InAppBrowserOptions } from "@ionic-native/in-app-browser";
import { OtherService } from "../../services/other.service";

@Component({
  selector: 'signIn-page',
  templateUrl: 'signIn.html'
})
export class SignInPage {
  response: any[];
  phone: string;
  password: string;
  agree: boolean = false;
  exists: any = null;

  constructor(
    public navCtrl: NavController,
    public platform: Platform,
    private storage: Storage,
    private alertCtrl: AlertController,
    private translate: TranslateService,
    private smsService: SmsService,
    private iab: InAppBrowser,
    private authService: AuthService,
    private otherService: OtherService
  ) {
    this.platform.registerBackButtonAction(() => {},1);
  }

  ionViewDidLoad() {
    setTimeout(() =>
      document.querySelector("a").addEventListener('click', () => this.openOffer())
    , 1000);
  }

  checkNumber(element) {
    if (this.phone.length === 10) {
      element.blur();
      this.authService.checkNumber(this.phone).subscribe(
        response =>  this.exists = response.exists,
        error => this.otherService.handleError(error)
      );
    }
  }

  sendSms() {
    let alert = this.alertCtrl.create({
      subTitle: this.translate.instant('signIn.info', {phone: this.phone}),
      buttons: [
        {
          text: this.translate.instant('global.cancel'),
          handler: () => { alert.dismiss(); return false; }
        },
        {
          text: this.translate.instant('signIn.continue'),
          handler: () => {
            this.smsService.sendSms('7'+this.phone).subscribe(
              (response: SignInPage) => this.navCtrl.push(EnterSmsCodePage, { data: response }),
              error => this.otherService.handleError(error)
            );
          }
        }
      ]
    });
    alert.present();
  }

  initializeUser() {
    this.authService.initUser('7'+this.phone).subscribe(
      res => this.sendSms(),
      err => this.otherService.handleError(err)
    );
  }

  checkForm() {
    if (this.phone)
      return !this.password || (this.phone && this.phone.length !== 10);
    else
      return true;
  };

  async signIn() {
    this.authService.signIn({phone: 7 + this.phone, password: this.password}).subscribe(
      async response => {
        await this.storage.set('token', response.token);
        this.storage.set('user', response.user);
        this.navCtrl.push(TabsPage);
      },
      error => {
        console.log(error.error);
        let alert = this.alertCtrl.create({
          title: this.translate.instant('signIn.incorrectPassword'),
          buttons: [
            {
              text: this.translate.instant('signIn.tryAgain'),
              role: 'cancel'
            }
          ]
        });
        alert.present();
      }
    );
  };

  openOffer() {
    const options: InAppBrowserOptions = {
      zoom: 'no',
      hardwareback: 'yes',
      location: 'yes'
    };
    const url = encodeURIComponent('https://tastamat.kz/offer.pdf');
    this.iab.create('https://docs.google.com/viewer?url=' + url, '_blank', options);
  }
}
