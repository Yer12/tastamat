import { Component } from '@angular/core';
import {AlertController, NavController, NavParams} from 'ionic-angular';
import { TranslateService } from "@ngx-translate/core";
import { Storage } from "@ionic/storage";
import { TabsPage } from "../tabs/tabs";
import { AuthService } from "../../services/auth.service";

@Component({
  selector: 'setPassword-page',
  templateUrl: 'setPassword.html'
})
export class SetPasswordPage {
  password: string;
  passwordR: string;
  data = {
    id: null,
    phone: null,
    code: null
  };

  constructor (
    public navCtrl: NavController, private navParams: NavParams, private translate: TranslateService,
    private alertCtrl: AlertController, private authService: AuthService, private storage: Storage
  ) {
    this.data = this.navParams.get('data');
  }

  checkPassword() {
    const data = {
      id: this.data.id,
      phone: this.data.phone,
      password: this.password
    };

    if (this.password !== this.passwordR) {
      let alert = this.alertCtrl.create({
        subTitle: this.translate.instant('setPassword.incorrectPass'),
        buttons: [
          {
            text: this.translate.instant('setPassword.ok'),
            handler: () => { alert.dismiss();
              return false; }
          }
        ]
      });
      alert.present();
    }
    else {
      this.authService.setPassword(data).subscribe(
        response => {
          let alert = this.alertCtrl.create({
            subTitle: this.translate.instant('setPassword.success'),
            buttons: [
              {
                text: this.translate.instant('setPassword.signIn'),
                handler: () => {
                  this.authService.signIn({phone: this.data.phone, password: this.password}).subscribe(
                    async response => {
                      await this.storage.set('token', response.token);
                      this.storage.set('user', response.user);
                      this.navCtrl.push(TabsPage);
                    },
                    error => console.log(error.error)
                  );
                }
              }
            ]
          });
          alert.present();
        },
        error => {
          console.log(error.error);
        }
      );
    }
  }



}
