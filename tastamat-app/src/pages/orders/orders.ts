import { Component } from '@angular/core';
import { LoadingController, NavController } from 'ionic-angular';
import { Storage } from "@ionic/storage";
import { ProfilePage } from "../profile/profile";
import {QrScannerPage} from "../qrScanner/qrScanner";

@Component({
  selector: 'page-orders',
  templateUrl: 'orders.html'
})
export class OrdersPage {
  orders = [];
  filteredOrders = [];
  page: number = 0;
  limit: number;
  showMore: boolean = false;
  status: string = 'current';

  constructor(
    public navCtrl: NavController,
    private storage: Storage,
    public loadingCtrl: LoadingController
  ) {
    // this.limit = screen.height
    //   ? Math.round((screen.height - 300)/64)
    //   : 5;
  }

  ionViewDidLoad() {
    this.getOrders(false);
  }

  async doRefresh(refresher) {
    this.page = await 0;
    this.getOrders(true);
    setTimeout(() => {
      refresher.complete();
    }, 2000);
  }

  getOrders(refresh) {
    this.storage.get('token').then(async token => {
      if (token) {
        let loader = this.loadingCtrl.create({ spinner: 'crescent' });
        loader.present();

        this.orders = [
          {id: 1, phone: '+7 777 642 03 44', date: new Date, rating: 3, status: 'finished', recipient: 'Тимур', locker: {name: 'ЖК “Лазурный квартал”', address: 'Сарайшык 7/3'}},
          {id: 2, phone: '+7 701 220 28 21', date: new Date, rating: 4, status: 'current', recipient: 'Арман', locker: {name: 'ЖК “Лазурный квартал”', address: 'Сарайшык 7/3'}},
          {id: 3, phone: '+7 701 546 25 84', date: new Date, rating: 5, status: 'finished', recipient: 'Арман', locker: {name: 'ЖК “Лазурный квартал”', address: 'Сарайшык 7/3'}}
        ];

        this.sort(this.status);

        loader.dismiss();
      }
    });
  }

  goToQr() {
    const data = {
      type: 'pickParcel'
    };
    this.navCtrl.push(QrScannerPage, {data: data})
  }

  sort(value: string) {
    this.status = value;
    this.filteredOrders = this.orders.filter(o => {
      return o.status === value
    })
  }

  async loadMore() {
    await this.page++;
    this.getOrders(false);
  }

  goToProfile() {
    this.navCtrl.push(ProfilePage);
  }

  expand(item) {
    const button = document.getElementById(item.id+item.recipient);
    if (button.className.indexOf('expanded') > -1) {
      button.classList.remove('expanded');
    } else {
      const otherButtons = document.querySelectorAll('.tastamatApp__list__item');
      for (let i = 0; i < otherButtons.length; i++) {
        otherButtons[i].classList.remove('expanded');
      }
      button.classList.add('expanded');
    }
  }

}
