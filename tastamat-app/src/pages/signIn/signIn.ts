import { Component } from '@angular/core';
import { LoadingController, NavController, Platform } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { AlertController } from 'ionic-angular';
import { TranslateService } from "@ngx-translate/core";
import { EnterSmsCodePage } from "../enterSmsCode/enterSmsCode";
import { SmsService } from "../../services/sms.service";
import { AuthService } from "../../services/auth.service";
import { TabsPage } from "../tabs/tabs";


@Component({
  selector: 'signIn-page',
  templateUrl: 'signIn.html'
})
export class SignInPage {
  response: any[];
  phone: string;
  password: string;
  agree: boolean;
  exists: any = null;

  constructor(
    public navCtrl: NavController,
    public platform: Platform,
    private storage: Storage,
    private alertCtrl: AlertController,
    private translate: TranslateService,
    private smsService: SmsService,
    private authService: AuthService,
    private loadingCtrl: LoadingController
  ) {
    this.platform.registerBackButtonAction(() => {},1);
  }

  checkNumber(element) {
    if (this.phone.length === 10) {
      element.blur();
      let loader = this.loadingCtrl.create({ spinner: 'crescent' });
      loader.present();

      this.authService.checkNumber(this.phone).subscribe(
        response => { this.exists = response.exists; loader.dismiss(); },
        error => { console.log(error); loader.dismiss(); }
      );
    }
  }

  sendSms() {
    let alert = this.alertCtrl.create({
      subTitle: this.translate.instant('signIn.info', {phone: this.phone}),
      buttons: [
        {
          text: this.translate.instant('signIn.cancel'),
          handler: () => { alert.dismiss(); return false; }
        },
        {
          text: this.translate.instant('signIn.continue'),
          handler: () => {
            this.smsService.sendSms('7'+this.phone).subscribe(
              (response: SignInPage) => { this.navCtrl.push(EnterSmsCodePage, {data: response}) },
              error => console.log(error) // error path
            );
          }
        }
      ]
    });
    alert.present();
  }


  checkForm() {
    if (this.phone)
      return !this.password || (this.phone && this.phone.length !== 10) || !this.agree;
    else
      return true;
  };

  async signIn() {
    let loader = this.loadingCtrl.create({
      spinner: 'crescent'
    });

    loader.present();
    this.authService.signIn({phone: 7 + this.phone, password: this.password}).subscribe(
      async response => {
        loader.dismiss();
        await this.storage.set('token', response.token);
        this.storage.set('user', response.user);
        this.navCtrl.push(TabsPage);
      },
      error => {
        console.log(error.error);
        loader.dismiss();
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
}
