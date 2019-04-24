import { Component } from '@angular/core';
import { NavController, Modal, ModalController } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { SignInPage } from "../signIn/signIn";
import { SmsTemplatePage } from "../smsTemplate/smsTemplate";
import { AuthService } from "../../services/auth.service";
import { OtherService } from "../../services/other.service";

@Component({
  selector: 'page-profile',
  templateUrl: 'profile.html'
})
export class ProfilePage {
  price: number;
  user: {
    id: number;
    phone: string;
    profile: {
      id: number,
      template: string,
      wallet: number
    }
  };

  constructor(
    public navCtrl: NavController,
    private storage: Storage,
    private modal: ModalController,
    private authService: AuthService,
    private otherService: OtherService
  ) {
    this.storage.get('user').then(user => {
      if (user) {
        this.user = user;
        this.price = user.profile.price || 250;
      }
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
    this.authService.getAccount().subscribe(
      res => {
        this.storage.set('user', res);
        this.storage.set('profile', res.profile);
        this.user = res;
      },
      err => this.otherService.handleError(err)
    );
  };

  goTo() {
    this.navCtrl.push(SmsTemplatePage);
  }

  fillWallet() {
    const myModal: Modal = this.modal.create('PaymentPage',
      { type: 'fillUp', price: this.price }
    );
    myModal.present();
    myModal.onWillDismiss(data => this.getProfileInfo())
  }

  showHistory() {
    const myModal: Modal = this.modal.create('PaymentPage', { 'type': 'history' });
    myModal.present();
    myModal.onWillDismiss(data => this.getProfileInfo())
  }

  async signOut() {
    await this.storage.remove('token');
    this.navCtrl.push(SignInPage);
  }

}
