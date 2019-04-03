import { Component } from '@angular/core';
import { NavController, Modal, ModalController } from 'ionic-angular';
import { QrScannerPage } from "../qrScanner/qrScanner";
import { OtherService } from "../../services/other.service";

@Component({
  selector: 'page-addParcel',
  templateUrl: 'addParcel.html'
})
export class AddParcelPage {
  name: string;
  phone: string;
  cellSize: string;

  constructor(
    public navCtrl: NavController,
    private modal: ModalController,
    private otherService: OtherService
  ) {}

  setCellSize(size) {
    this.cellSize = size;
  }

  checkForm() {
    if (this.phone)
      return !this.name || (this.phone && this.phone.length !== 10) || !this.cellSize;
    else
      return true;
  }

  goToQr() {
    const data = {
      type: 'addParcel',
      name: this.name,
      phone: 7 + this.phone,
      cellSize: this.cellSize
    };
    this.navCtrl.push(QrScannerPage, {data: data})
  }

  openModal() {
    const ManualInputPage: Modal = this.modal.create('ManualInputPage', { 'type': 'accept' });
    ManualInputPage.present();

    ManualInputPage.onWillDismiss(data => {
      if (data)
        this.openCell(data.toUpperCase());
    })
  }

  async openCell(presenceCode) {
    const data = await {
      recipientName: this.name,
      recipientPhone: this.phone,
      size: this.cellSize,
      locker: presenceCode
    };
    this.otherService.openCell(data).subscribe(
      () => this.otherService.cellOpenedAlert(),
      err => this.otherService.handleError(err)
    );
  }
}
