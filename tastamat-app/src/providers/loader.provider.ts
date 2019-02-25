import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';
import { LoadingController } from 'ionic-angular';

@Injectable()
export class LoaderProvider {

  loading: any;

  constructor(public loadingCtrl: LoadingController) {}

  show() {
    this.loading = this.loadingCtrl.create({ spinner: 'crescent' });
    this.loading.present();
  }

  hide() {
    if (this.loading)
      this.loading.dismissAll();
  }

}
