import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { SmsService } from "../../services/sms.service";
import { SetPasswordPage } from "../setPasswordPage/setPassword";
import { OtherService } from "../../services/other.service";

@Component({
  selector: 'enterSmsCode-page',
  templateUrl: 'enterSmsCode.html'
})
export class EnterSmsCodePage {
  exists: boolean;
  phone: string;
  code: number;
  smsCode = {
    1: '',
    2: '',
    3: '',
    4: ''
  };
  sendAgain: boolean = false;
  data = {
    id: null,
    phone: null,
    code: null
  };
  type: string;

  constructor(
    public navCtrl: NavController,
    private navParams: NavParams,
    private smsService: SmsService,
    private otherService: OtherService

  ) {
    this.data = this.navParams.get('data') || {};
    this.type = this.navParams.get('type') || '';
  }

  popView(){
    this.navCtrl.pop();
  }

  pasteFunc(event) {
    setTimeout(() => {
      let arr = event.target.value.split("");
      for (let i=1; i < 5; i++) {
        this.smsCode[i] = arr[i - 1] || '';
      }
      this.confirmCode();
    }, 100);
  }

  changeFocus(event) {
    const element = event.target;
    const idName = element.id.substr(0,element.id.length - 1);
    const id = parseInt(element.id.substr(element.id.length - 1));
    const nextId = id + 1, prevId = id - 1;

    if (element.value.length === 4) {
      this.pasteFunc(event);
    } else  {
      if (element.value.length > 1) {
        this.smsCode[id] = element.value.substr(0, 1);
        this.smsCode[nextId] = element.value.substr(element.value.length - 1, element.value.length);
      }
      if (element.value && nextId < 5) document.getElementById(idName+nextId).focus();
      else if (!element.value && prevId > 0) document.getElementById(idName+prevId).focus();
      this.confirmCode();
    }
  }

  sendSms() {
    this.smsService.sendSms(this.data.phone).subscribe(
      response => this.sendAgain = false,
      error => this.otherService.handleError(error)
    );
  }

  async confirmCode() {
    if (this.smsCode["1"] && this.smsCode["2"] && this.smsCode["3"] && this.smsCode["4"]) {
      this.data.code = await this.smsCode["1"] + this.smsCode["2"] + this.smsCode["3"] + this.smsCode["4"];
      this.smsService.confirmCode(this.data).subscribe(
        response => this.navCtrl.push(SetPasswordPage, { data: this.data }),
        error => this.otherService.handleError(error)
      );
    }
  }

}
