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

  checkNumber() {
    this.authService.checkNumber(this.formatPhoneNumber(this.phone)).subscribe(
      response =>  this.exists = response.exists,
      error => this.otherService.handleError(error)
    );
  }

  sendSms() {
    let alert = this.alertCtrl.create({
      subTitle: this.translate.instant('signIn.info', {phone: this.formatPhoneNumber(this.phone)}),
      buttons: [
        {
          text: this.translate.instant('global.cancel'),
          handler: () => { alert.dismiss(); return false; }
        },
        {
          text: this.translate.instant('signIn.continue'),
          handler: () => {
            this.smsService.sendSms(this.formatPhoneNumber(this.phone)).subscribe(
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
    this.authService.initUser(this.formatPhoneNumber(this.phone)).subscribe(
      res => this.sendSms(),
      err => this.otherService.handleError(err)
    );
  }

  checkForm() {
    let phone = this.formatPhoneNumber(this.phone)
    if (phone)
      return !this.password || !this.phoneNumberValid(phone);
    else
      return true;
  };

  async signIn() {
    this.authService.signIn({phone: this.formatPhoneNumber(this.phone), password: this.password}).subscribe(
      async response => {
        await this.storage.set('token', response.token);
        this.storage.set('user', response.user);
        this.storage.set('profile', response.user.profile);
        this.navCtrl.push(TabsPage);
      },
      error => this.otherService.handleError(error)
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

  formatPhoneNumber(phone) {
    if (phone) {
      let formattedPhoneNumber = phone.replace(/\D/g,'')
      return formattedPhoneNumber.length > 0 ? formattedPhoneNumber : null
    } else {
      return null
    }
  }

  phoneNumberValid(phone) {
    let phoneToCheck = this.formatPhoneNumber(phone)
    return phoneToCheck && phoneToCheck.length >= 10
  }
}
