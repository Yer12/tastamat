import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { ProfilePage } from "../profile/profile";
import { QrScannerPage } from "../qrScanner/qrScanner";
import { OtherService } from "../../services/other.service";

@Component({
  selector: 'page-orders',
  templateUrl: 'orders.html'
})
export class OrdersPage {
  orders = {
    count: 0,
    list: []
  };
  page: number = 0;
  limit: number;
  showMore: boolean = false;
  status: string = 'SENT';

  constructor(public navCtrl: NavController, private otherService: OtherService) {
    this.limit = screen.height
      ? Math.round((screen.height - 300)/64)
      : 5;
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
    this.otherService.getOrders(this.status, this.page, this.limit).subscribe(
      async response => {
        if (this.orders.list.length && !refresh)
          response.list.forEach(order => { this.orders.list.push(order); });
        else
          this.orders = await response;

        this.showMore = !(response.count === this.orders.list.length);
      },
      err => this.otherService.handleError(err)
    );
  }

  goToQr(id) {
    const data = {
      type: 'pickParcel',
      id: id
    };
    this.navCtrl.push(QrScannerPage, {data: data})
  }

  async loadMore() {
    await this.page++;
    this.getOrders(false);
  }

  goToProfile() {
    this.navCtrl.push(ProfilePage);
  }

  expand(item) {
    const button = document.getElementById(item.id+item.recipientPhone);
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
