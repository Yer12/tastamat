import { Component, ViewChild } from '@angular/core';
import { IonicPage, ViewController, NavParams, Platform } from 'ionic-angular';
import { InAppBrowser, InAppBrowserOptions } from '@ionic-native/in-app-browser';
import { PaymentService } from "../../services/payment.service";
import { OtherService} from "../../services/other.service";

@IonicPage()
@Component({
  selector: 'page-payment',
  templateUrl: 'payment.html',
})
export class PaymentPage {
  amount: number;
  type: string;
  page: number = 0;
  limit: number;
  price: number;
  showMore: boolean = false;
  history = {
    count: 0,
    list: []
  };
  payment: {
    amount: string
    id: number
    payment: string
    url: string
  };

  @ViewChild('amountInput') amountInput;

  constructor(
    private view: ViewController,
    private iab: InAppBrowser,
    private paymentService: PaymentService,
    private otherService: OtherService,
    private navParams: NavParams,
    public platform: Platform
  ) {
    this.type = this.navParams.get('type');
    this.price = this.navParams.get('price');
    this.limit = screen.height
      ? Math.round((screen.height - 70)/64)
      : 5;
    this.platform.registerBackButtonAction(() => this.closeModal(),1);
  }

  ngAfterViewChecked() {
    if (this.type === 'fillUp')
      this.amountInput.setFocus()
  }

  ionViewWillEnter() {
    if (this.type === 'history')
      this.getPaymentHistory(true);
  }

  async doRefresh(refresher) {
    this.page = await 0;
    this.getPaymentHistory(true);
    setTimeout(() => {
      refresher.complete();
    }, 1000);
  }

  closeModal() {
    this.view.dismiss();
  }

  createPayment() {
    this.paymentService.fillUpWallet(this.amount).subscribe(
      res => {
        this.payment = res;
        this.openBrowser(res.url)
      },
      err => this.otherService.handleError(err)
    )
  }

  openBrowser(url) {
    this.closeModal();
    const options: InAppBrowserOptions = {
      zoom: 'no',
      hardwareback: 'no',
      location: 'no'
    };
    const browser = this.iab.create(url, '_blank', options);
    browser.on('loadstart').subscribe(event => {
      if (event.url === 'https://tastamat.kz/') {
        this.paymentService.successPayment(this.payment.id).subscribe(
          res => console.log(res),
          err => this.otherService.handleError(err)
        );
        browser.close();
      }
    });
  }

  getPaymentHistory(refresh) {
    this.paymentService.getPaymentHistory(this.page, this.limit).subscribe(
      async response => {
        if (this.history.list.length && !refresh)
          response.list.forEach(order => { this.history.list.push(order); });
        else
          this.history = await response;

        this.showMore = !(response.count === this.history.list.length);
      },
      err => this.otherService.handleError(err)
    )
  }

  getCurrentStatus(id) {
    this.paymentService.getCurrentStatus(id).subscribe(
      res => {
        const index = this.history.list.findIndex(item => item.id === res.id);
        this.history.list.splice(index, 1, res);
      },
      err => this.otherService.handleError(err)
    )
  }

  async loadMore() {
    await this.page++;
    this.getPaymentHistory(false);
  }

}
