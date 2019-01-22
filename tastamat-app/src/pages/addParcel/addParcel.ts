import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { QrScannerPage } from "../qrScanner/qrScanner";

@Component({
  selector: 'page-addParcel',
  templateUrl: 'addParcel.html'
})
export class AddParcelPage {
  name: string;
  phone: string;
  cellSize: string;

  constructor(public navCtrl: NavController) {

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
    this.navCtrl.push(QrScannerPage, {data: data})
  }
}
