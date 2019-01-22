import { Component } from '@angular/core';
import { AlertController, LoadingController, NavController, NavParams, Platform} from 'ionic-angular';
import { BarcodeScanner, BarcodeScannerOptions } from '@ionic-native/barcode-scanner';
import { TranslateService } from "@ngx-translate/core";
import { Storage } from '@ionic/storage';
import { OtherService } from "../../services/other.service";
import { ProfilePage } from "../profile/profile";
import { AddParcelPage } from "../addParcel/addParcel";

@Component({
  selector: 'qrScanner-page',
  templateUrl: 'qrScanner.html'
})
export class QrScannerPage {
  scannedData = {};
  option: BarcodeScannerOptions;
  presenceCode: string;
  data: {
    type: string,
    name: string,
    phone: string,
    cellSize: string,
  };

  constructor(
    public navCtrl: NavController, private navParams: NavParams, public platform: Platform,
    private barcodeScanner: BarcodeScanner, private loadingCtrl: LoadingController, private alertCtrl: AlertController,
    private translate: TranslateService, private otherService: OtherService, private storage: Storage
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
    this.barcodeScanner.scan(this.option).then(barcodeData => {
      this.scannedData = barcodeData;
      const link = barcodeData && barcodeData.text;
      if (link && link.toLowerCase().indexOf('2qr.kz') > -1) {
        const presenceCode = link.split('/');
        this.presenceCode = presenceCode[presenceCode.length - 1];

        this.openCell({
          recipientName: this.data.name,
          recipientPhone: this.data.phone,
          size: this.data.cellSize,
          presenceCode: this.presenceCode
        });
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

  openCell(data) {
    this.storage.get('token').then((token) => {
      if (token) {
        let loader = this.loadingCtrl.create({ spinner: 'crescent' });
        loader.present();
        this.otherService.openCell(token, data).subscribe(
          response => {
            loader.dismiss();
            let alert = this.alertCtrl.create({
              subTitle: this.translate.instant('qrScanner.cellOpened'),
              buttons: [
                {
                  text: this.translate.instant('qrScanner.ok'),
                  handler: () => {
                    alert.dismiss();
                    this.navCtrl.push(ProfilePage);
                    return false;
                  }
                }
              ]
            });
            alert.present();
          },
          error => {
            loader.dismiss();
            console.log(error);
            const err = JSON.parse(error.error.message);
            let alert = this.alertCtrl.create({
              subTitle: err[this.translate.getDefaultLang()],
              buttons: [
                {
                  text: this.translate.instant('qrScanner.ok'),
                  handler: () => {
                    alert.dismiss();
                    this.popView();
                    return false;
                  }
                }
              ]
            });
            alert.present();
          }
        );
      }
    });
  }


}
