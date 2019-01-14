import { Component } from '@angular/core';
import {AlertController, LoadingController, NavController, NavParams} from 'ionic-angular';
import { SmsService } from "../../services/sms.service";
import {SetPasswordPage} from "../setPasswordPage/setPassword";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'enterSmsCode-page',
  templateUrl: 'enterSmsCode.html'
})
export class EnterSmsCodePage {
  exists: boolean;
  phone: string;
  code: number;
  smsCode1: string;
  smsCode2: string;
  smsCode3: string;
  smsCode4: string;
  sendAgain: boolean = false;
  data = {
    id: null,
    phone: null,
    code: null
  };

  constructor(
    public navCtrl: NavController,
    private navParams: NavParams,
    private smsService: SmsService,
    private alertCtrl: AlertController,
    private translate: TranslateService,
    private loadingCtrl: LoadingController

  ) {
    this.data = this.navParams.get('data');
  }

  popView(){
    this.navCtrl.pop();
  }

  changeFocus(element) {
    const idName = element.id.substr(0,element.id.length - 1);
    const nextId = parseInt(element.id.substr(element.id.length - 1)) + 1;
    const prevId = parseInt(element.id.substr(element.id.length - 1)) - 1;

    if (element.value && nextId < 5) {
      document.getElementById(idName+nextId).focus();
    }
    else if (!element.value && prevId > 0) {
      document.getElementById(idName+prevId).focus();
    }
  }

  sendSms() {
    this.smsService.sendSms(this.data.phone).subscribe(
      response => {
        this.sendAgain = false;
      },
      error => console.log(error) // error path
    );
  }

  confirmCode() {
    if (this.smsCode1 && this.smsCode2 && this.smsCode3 && this.smsCode4) {
      this.data.code = this.smsCode1 + this.smsCode2 + this.smsCode3 + this.smsCode4;

      let loader = this.loadingCtrl.create({ spinner: 'crescent' });
      loader.present();
      this.smsService.confirmCode(this.data).subscribe(
        response => {
          loader.dismiss();
          this.navCtrl.push(SetPasswordPage, {data: this.data})
        },
        error => {
          loader.dismiss();
          const err = JSON.parse(error.error.message);
          let alert = this.alertCtrl.create({
            subTitle: err[this.translate.getDefaultLang()],
            buttons: [
              {
                text: this.translate.instant('alerts.ok'),
                handler: () => { alert.dismiss();
                  return false; }
              }
            ]
          });
          alert.present();
        }
      );
    }
  }

}
