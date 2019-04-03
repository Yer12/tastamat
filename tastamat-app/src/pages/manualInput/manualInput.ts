import { Component } from '@angular/core';
import { IonicPage, ViewController } from 'ionic-angular';

@IonicPage()
@Component({
  selector: 'page-manualInput',
  templateUrl: 'manualInput.html',
})
export class ManualInputPage {
  presenceCode: string;

  constructor(private view: ViewController) {}

  closeModal(empty) {
    this.view.dismiss(empty ? null : this.presenceCode);
  }

}
