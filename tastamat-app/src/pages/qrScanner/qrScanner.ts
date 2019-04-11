import { Component } from '@angular/core';
import {
  IonicPage,
  AlertController,
  ViewController,
  NavParams,
  Platform
} from 'ionic-angular';
import { BarcodeScanner, BarcodeScannerOptions } from '@ionic-native/barcode-scanner';
import { TranslateService } from "@ngx-translate/core";
import { OtherService } from "../../services/other.service";

@IonicPage()
@Component({
  selector: 'qrScanner-page',
  templateUrl: 'qrScanner.html'
})
export class QrScannerModal {
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
  showError: boolean = false;

  constructor(
    private view: ViewController,
    private navParams: NavParams,
    public platform: Platform,
    private barcodeScanner: BarcodeScanner,
    private alertCtrl: AlertController,
    private translate: TranslateService,
    private otherService: OtherService
  ) {
    this.platform.registerBackButtonAction(this.closeModal,1);

    this.data = this.navParams.get('data');
  }

  ionViewWillEnter() {
    this.scan();
  }

  closeModal(data?: any) {
    this.showError = false;
    this.view.dismiss(data ? data : null);
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
        } else if (barcodeData.cancelled == true) {
          this.closeModal()
        }
    }).catch(err => {
      console.log('Error', err);
      this.showError = true;
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
        this.closeModal();
      },
      err => {
        this.otherService.handleError(err);
        this.closeModal();
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
        this.otherService.cellOpenedAlert("withdrawn");
        this.closeModal();
      },
      err => {
        this.otherService.handleError(err);
        this.closeModal();
      }
    )
  }
}
