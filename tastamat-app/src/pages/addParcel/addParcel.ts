import { Component } from '@angular/core';
import { Modal, ModalController } from 'ionic-angular';
import { OtherService } from "../../services/other.service";
import { Storage } from "@ionic/storage";

@Component({
  selector: 'page-addParcel',
  templateUrl: 'addParcel.html'
})
export class AddParcelPage {
  name: string;
  phone: string;
  cellSize: string;
  user: {
    id: number;
    phone: string;
    role: string
    system: {};
  };

  constructor(
    private modal: ModalController,
    private otherService: OtherService,
    private storage: Storage
  ) {
  }

  ionViewDidEnter() {
    this.storage.get('user').then(user => {
      if (user) { this.user = user; }
    });
  }

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
    const QrScannerModal: Modal = this.modal.create('QrScannerModal', { data: data });
    QrScannerModal.present();

    QrScannerModal.onWillDismiss(data => {
      if (data === 'error')
        this.openModal();
    })
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
      recipientPhone: 7 + this.phone,
      size: this.cellSize,
      locker: presenceCode
    };
    this.otherService.openCell(data).subscribe(
      () => this.otherService.cellOpenedAlert(),
      err => this.otherService.handleError(err)
    );
  }

  fillWallet() {
    const myModal: Modal = this.modal.create('PaymentPage', { 'type': 'fillUp' });
    myModal.present();
  }
}
