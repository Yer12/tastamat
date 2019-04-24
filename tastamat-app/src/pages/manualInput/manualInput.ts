import { Component } from '@angular/core';
import { IonicPage, Platform, ViewController } from 'ionic-angular';

@IonicPage()
@Component({
  selector: 'page-manualInput',
  templateUrl: 'manualInput.html',
})
export class ManualInputPage {
  presenceCode: string;

  constructor(private view: ViewController, public platform: Platform) {
    this.platform.registerBackButtonAction(() => this.closeModal(true),1);
  }

  closeModal(empty) {
    this.view.dismiss(empty ? null : this.presenceCode);
  }

}
