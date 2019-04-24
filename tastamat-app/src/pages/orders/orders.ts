import { Component } from '@angular/core';
import {
  Modal,
  NavController,
  AlertController,
  ModalController
} from 'ionic-angular';
import { ProfilePage } from "../profile/profile";
import { OtherService } from "../../services/other.service";
import { TranslateService } from "@ngx-translate/core";

@Component({
  selector: 'page-orders',
  templateUrl: 'orders.html'
})
export class OrdersPage {
  orders = {
    count: 0,
    list: []
  };
  orderId: number;
  page: number = 0;
  limit: number;
  showMore: boolean = false;
  status: string = 'SENT';

  constructor(
    public navCtrl: NavController,
    private otherService: OtherService,
    private modal: ModalController,
    private alertCtrl: AlertController,
    private translate: TranslateService
  ) {
    this.limit = screen.height
      ? Math.round((screen.height - 300)/64)
      : 5;
  }

  ionViewDidLoad() {
    this.getOrders(false);
  }

  doRefresh(refresher) {
    this.reloadOrders();
    setTimeout(() => {
      refresher.complete();
    }, 2000);
  }

  async reloadOrders() {
    this.page = await 0;
    this.getOrders(true);
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

  chooseOpenType(id) {
    this.orderId = id;
    let alert = this.alertCtrl.create({
      subTitle: this.translate.instant('orders.chooseOpenType'),
      buttons: [
        {
          text: this.translate.instant('orders.manual'),
          handler: () => {
            this.openModal();
            alert.dismiss();
            return false;
          }
        },
        {
          text: this.translate.instant('orders.qr'),
          handler: () => {
            this.goToQr();
            alert.dismiss();
            return false;
          }
        }
      ]
    });
    alert.present();
  }

  goToQr() {
    const QrScannerModal: Modal = this.modal.create('QrScannerModal', { data: {
      type: 'pickParcel',
      id: this.orderId
    }});
    QrScannerModal.present();

    QrScannerModal.onWillDismiss(data => {
      if (data === 'error')
        this.openModal();
      else if (data)
        this.withdrawnFromCell(data);
    })
  }

  openModal() {
    const ManualInputPage: Modal = this.modal.create('ManualInputPage', { 'type': 'accept' });
    ManualInputPage.present();

    ManualInputPage.onWillDismiss(data => {
      if (data)
        this.withdrawnFromCell(data.toUpperCase());
    })
  }

  withdrawnFromCell(presenceCode) {
    this.otherService.withdrawFromCell({id: this.orderId, locker: presenceCode})
      .subscribe(
        res => {
          this.otherService.cellOpenedAlert("withdrawn");
          this.reloadOrders();
        },
        err => this.otherService.handleError(err)
      )
  }

  async loadMore() {
    await this.page++;
    this.getOrders(false);
  }

  goToProfile() {
    this.navCtrl.push(ProfilePage);
  }

  expand(item) {
    const button = document.getElementById(item.id+item.createDate);
    button.classList.toggle('expanded');
  }

}
