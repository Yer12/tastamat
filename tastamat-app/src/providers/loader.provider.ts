import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';
import { LoadingController } from 'ionic-angular';

@Injectable()
export class LoaderProvider {

  constructor(public loadingCtrl: LoadingController) {}

  loading: any;

  create() {
    this.loading = this.loadingCtrl.create({ spinner: 'crescent' });
  }

  show() {
    this.create();
    this.loading.present();
  }

  hide() {
    this.loading.dismiss();
  }

}
