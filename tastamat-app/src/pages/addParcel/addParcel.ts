import { Component } from '@angular/core';
import { Modal, ModalController } from 'ionic-angular';
import { OtherService } from "../../services/other.service";
import { Storage } from "@ionic/storage";

import { Contacts, Contact, ContactField } from '@ionic-native/contacts';

@Component({
  selector: 'page-addParcel',
  templateUrl: 'addParcel.html'
})
export class AddParcelPage {
  name: string;
  phone: string;
  cellSize: string;
  price: number;
  user: {
    id: number;
    phone: string;
    profile: {
      id: number,
      template: string,
      wallet: number
    }
  };

  constructor(
    private modal: ModalController,
    private otherService: OtherService,
    private storage: Storage,
    private contacts: Contacts
  ) {
  }

  ionViewDidEnter() {
    this.storage.get('user').then(user => {
      if (user) {
        this.user = user;
        this.price = user.profile.price || 250;
      }
    });
  }

  setCellSize(size) {
    this.cellSize = size;
  }

  checkForm() {
    let phone = this.formatPhoneNumber(this.phone)
    if (phone)
      return !this.name || !this.phoneNumberValid(phone) || !this.cellSize;
    else
      return true;
  }

  goToQr() {
    const data = {
      type: 'addParcel',
      name: this.name,
      phone: this.formatPhoneNumber(this.phone),
      cellSize: this.cellSize
    };
    const QrScannerModal: Modal = this.modal.create('QrScannerModal', { data: data });
    QrScannerModal.present();

    QrScannerModal.onWillDismiss(data => {
      if (data === 'error')
        this.openModal();
      else if (data)
        this.openCell(data);
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

  openCell(presenceCode) {
    this.otherService.openCell({
      recipientName: this.name,
      recipientPhone: this.formatPhoneNumber(this.phone),
      size: this.cellSize,
      presenceCode: presenceCode
    }).subscribe(
      () => this.otherService.cellOpenedAlert(),
      err => this.otherService.handleError(err)
    );
  }

  fillWallet() {
    const myModal: Modal = this.modal.create('PaymentPage', { 'type': 'fillUp' });
    myModal.present();
  }

  formatPhoneNumber(phone) {
    if (phone) {
      let formattedPhoneNumber = phone.replace(/\D/g,'')
      if (formattedPhoneNumber && formattedPhoneNumber.length > 0 && formattedPhoneNumber.charAt(0) === '8') {
        formattedPhoneNumber = '7' + formattedPhoneNumber.substring(1)
      }
      return formattedPhoneNumber.length > 0 ? formattedPhoneNumber : null
    } else {
      return null
    }
  }

  formatEnteredPhoneNumber(event) {
    if (event && event.target && event.target.value && !event.target.value.startsWith('+7')) {
      if (event.target.value.startsWith('8')) {
        event.target.value = '+7' + event.target.value.substring(1)
      } else if (event.target.value.startsWith('+8')) {
        event.target.value = '+7' + event.target.value.substring(2)
      } else if (event.target.value.length >= 2) {
        event.target.value = '+7' + event.target.value.substring(2)
      }
    }
  }

  phoneNumberValid(phone) {
    let phoneToCheck = this.formatPhoneNumber(phone)
    return phoneToCheck && phoneToCheck.length === 11 && phoneToCheck.charAt(0) === '7'
  }

  selectContact() {
    try {
      this.contacts.pickContact()
        .then((contact: Contact) => {
          this.name = contact.name ? contact.name.formatted : null
          this.phone = contact.phoneNumbers ? contact.phoneNumbers
            .reduce((a: string, b: ContactField) => {
              if (b.type && (b.type === 'mobile' || b.type === 'iPhone') && b.value && this.phoneNumberValid(b.value)) {
                return this.formatPhoneNumber(b.value)
              } else if (a === null && b.value && this.phoneNumberValid(b.value)) {
                return this.formatPhoneNumber(b.value)
              } else {
                return a
              }
            }, null) : null
          }).catch((err: any) => {

        });
    } catch(error) {
      alert('Error: ' + error);
    }
  }
}
