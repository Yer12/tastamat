import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TranslateService } from "@ngx-translate/core";
import { AlertController } from "ionic-angular";

const baseUrl = "https://platform.tastamat.com/platform/v1/rest";

@Injectable ()
export class OtherService {
  constructor(
    private http: HttpClient,
    private alertCtrl: AlertController,
    private translate: TranslateService
  ) {}

  getTastamats(lat, lng, page, limit, searchKey): any {
    return this.http.get(
      `${baseUrl}/lockers?page=${page}&limit=${limit}${
        lat && lng ? `&lat=${lat}&lng=${lng}` : ""
        }${searchKey ? `&searchKey=${searchKey}` : ""}`
    );
  }

  openCell(data): any {
    return this.http.post(`${baseUrl}/a/orders/book-drop`, data)
  }

  getOrders(status, page, limit): any {
    return this.http.get(`${baseUrl}/a/orders?status=${status}&page=${page}&limit=${limit}`)
  }

  withdrawFromCell(data): any {
    return this.http.put(`${baseUrl}/a/orders/withdraw`, data)
  }

  handleError(data): any {
    let errorMessage = "";
    try {
      errorMessage = JSON.parse(data.error.message)[this.translate.getDefaultLang()]
    }
    catch (e) {
      errorMessage = this.translate.instant('global.unexpectedError')
    }
    let alert = this.alertCtrl.create({
      subTitle: errorMessage,
      buttons: [
        {
          text: 'OK',
          handler: () => {
            alert.dismiss();
            return false;
          }
        }
      ]
    });
    alert.present();
  }

  cellOpenedAlert(type?: string) {
    let alert = this.alertCtrl.create({
      subTitle: type === "withdrawn"
        ? this.translate.instant('qrScanner.cellOpened2')
        : this.translate.instant('qrScanner.cellOpened'),
      buttons: [
        {
          text: this.translate.instant('qrScanner.ok'),
          handler: () => {
            alert.dismiss();
            return false;
          }
        }
      ]
    });
    alert.present();
  }

}
