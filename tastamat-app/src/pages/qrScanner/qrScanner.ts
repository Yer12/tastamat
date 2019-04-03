import { Component } from '@angular/core';
import { AlertController, NavController, NavParams, Platform} from 'ionic-angular';
import { BarcodeScanner, BarcodeScannerOptions } from '@ionic-native/barcode-scanner';
import { TranslateService } from "@ngx-translate/core";
import { OtherService } from "../../services/other.service";
import { ProfilePage } from "../profile/profile";
import { AddParcelPage } from "../addParcel/addParcel";
import {OrdersPage} from "../orders/orders";

@Component({
  selector: 'qrScanner-page',
  templateUrl: 'qrScanner.html'
})
export class QrScannerPage {
  scannedData = {};
  option: BarcodeScannerOptions;
  presenceCode: string;
  data: {
    id: number,
    type: string,
    name: string,
    phone: string,
    cellSize: string,
  };

  constructor(
    public navCtrl: NavController, private navParams: NavParams, public platform: Platform,
    private barcodeScanner: BarcodeScanner, private alertCtrl: AlertController,
    private translate: TranslateService, private otherService: OtherService
  ) {
    this.platform.registerBackButtonAction(this.popView,1);

    this.data = this.navParams.get('data');
    if (this.data.type === 'addParcel' && (!this.data.name || !this.data.phone || !this.data.cellSize)) {
      this.navCtrl.push(AddParcelPage);
    }
  }

  ionViewWillEnter() {
    this.scan();
  }

  popView(){
    this.navCtrl.pop();
  }

  scan() {
    this.option = {
      prompt: this.translate.instant('qrScanner.scan'),
      showFlipCameraButton: true,
      showTorchButton: true
    };

    this.barcodeScanner.scan(this.option)
      .then(async barcodeData => {
        this.scannedData = barcodeData;
        const link = barcodeData && barcodeData.text;
        if (link && link.toLowerCase().indexOf('2qr.kz') > -1) {
          const presenceCode = link.split('/');
          this.presenceCode = await presenceCode[presenceCode.length - 1];

          if (this.data.type === 'addParcel')
            this.openCell();
          else if (this.data.type === 'pickParcel')
            this.withdrawnFromCell();

        } else if (link && link.toLowerCase().indexOf('2qr.kz') < 0) {
          let alert = this.alertCtrl.create({
            subTitle: this.translate.instant('qrScanner.incorrectQR'),
            buttons: [
              {
                text: this.translate.instant('qrScanner.tryAgain'),
                handler: () => {
                  alert.dismiss();
                  return false;
                }
              }
            ]
          });
          alert.present();
        } else if(barcodeData.cancelled == true) {
          this.popView()
        }
    }).catch(err => {
      console.log('Error', err);
    });
  }

  async openCell() {
    const data = await {
      recipientName: this.data.name,
      recipientPhone: this.data.phone,
      size: this.data.cellSize,
      locker: this.presenceCode
    };
    this.otherService.openCell(data).subscribe(
      res => {
        this.otherService.cellOpenedAlert();
        this.navCtrl.push(ProfilePage);
      },
      err => {
        this.otherService.handleError(err);
        this.popView();
      }
    );
  }

  async withdrawnFromCell() {
    const data = await {
      id: this.data.id,
      locker: this.presenceCode
    };
    this.otherService.withdrawFromCell(data).subscribe(
      res => {
        this.otherService.cellOpenedAlert();
        this.navCtrl.push(OrdersPage);
      },
      err => {
        this.otherService.handleError(err);
        this.popView();
      }
    )
  }
}
