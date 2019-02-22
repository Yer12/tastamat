import { Component } from '@angular/core';

import { TastamatsPage } from '../tastamats/tastamats';
import { AddParcelPage } from '../addParcel/addParcel';
import { OrdersPage } from '../orders/orders';
import { ProfilePage } from '../profile/profile';
import {Verification_step1Page} from "../verification/verification";
import {Storage} from "@ionic/storage";
import {NavController} from "ionic-angular";

@Component({
  selector: 'tabs',
  templateUrl: 'tabs.html'
})
export class TabsPage {

  tab1Root = TastamatsPage;
  tab2Root = AddParcelPage;
  tab3Root = OrdersPage;
  tab4Root = ProfilePage;

  constructor(private storage: Storage, public navCtrl: NavController) {
    this.storage.get('user').then((user) => {
      if (!user.verificated)
        this.navCtrl.push(Verification_step1Page);
    });
  }
}
