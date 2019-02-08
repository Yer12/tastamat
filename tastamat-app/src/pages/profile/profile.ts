import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { InAppBrowser, InAppBrowserOptions } from '@ionic-native/in-app-browser/ngx';
import { SignInPage } from "../signIn/signIn";
import { SmsTemplatePage } from "../smsTemplate/smsTemplate";
import { ProfileService } from "../../services/profile.service";

@Component({
  selector: 'page-profile',
  templateUrl: 'profile.html'
})
export class ProfilePage {
  profile: {
    id: number;
    wallet: number;
    template: string;
  };

  constructor(
    public navCtrl: NavController,
    private storage: Storage,
    private profileService: ProfileService,
    private iab: InAppBrowser
  ) {
    this.storage.get('profile').then(profile => {
      if (profile) { this.profile = profile; }
    });
  }

  ionViewWillEnter() {
    this.getProfileInfo();
  }

  doRefresh(refresher) {
    this.getProfileInfo();

    setTimeout(() => {
      refresher.complete();
    }, 2000);
  }

  getProfileInfo() {
    this.storage.get('user').then(user => {
      if (user) {
        this.profileService.getUserProfile(user.id).subscribe(
          res => {
            this.profile = res;
            this.storage.set('profile', res);
          },
          err => {
            console.log(err);
          }
        );

      }
    })
  };

  goTo() {
    this.navCtrl.push(SmsTemplatePage);
  }

  fillWallet() {
    const option: InAppBrowserOptions = {
      zoom: 'no',
      hardwareback: 'no'
    };
    const browser = this.iab.create('https://ionicframework.com/', '_self', option);

    browser.on('loadstart').subscribe(event => {
      if ((event.url).indexOf("http://localhost/callback") > -1) {
        browser.close();
      }
    });
  }

  async signOut() {
    await this.storage.remove('token');
    this.navCtrl.push(SignInPage);
  }

}
