import { Component } from '@angular/core';

import { TastamatsPage } from '../tastamats/tastamats';
import { AddParcelPage } from '../addParcel/addParcel';
import { ProfilePage } from '../profile/profile';

@Component({
  selector: 'tabs',
  templateUrl: 'tabs.html'
})
export class TabsPage {

  tab1Root = TastamatsPage;
  tab2Root = AddParcelPage;
  tab3Root = ProfilePage;

  constructor() {

  }
}
