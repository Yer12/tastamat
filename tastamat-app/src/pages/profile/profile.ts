import { Component } from '@angular/core';
import { NavController, Modal, ModalController } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { SignInPage } from "../signIn/signIn";
// import { SmsTemplatePage } from "../smsTemplate/smsTemplate";
import { AuthService } from "../../services/auth.service";

@Component({
  selector: 'page-profile',
  templateUrl: 'profile.html'
})
export class ProfilePage {
  user: {
    id: number;
    phone: string;
    role: string;
    system: {
      price: number;
    };
  };

  constructor(
    public navCtrl: NavController,
    private storage: Storage,
    private modal: ModalController,
    private authService: AuthService
  ) {
    this.storage.get('user').then(user => {if (user) { this.user = user; }});
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
        this.user = res;
      },
      err => {
        console.log(err);
      }
    );
  };

  // goTo() {
  //   this.navCtrl.push(SmsTemplatePage);
  // }

  fillWallet() {
    const myModal: Modal = this.modal.create('PaymentPage',
      { type: 'fillUp', price: this.user.system.price }
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
