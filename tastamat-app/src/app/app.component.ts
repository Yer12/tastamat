import {Component, ViewChild} from '@angular/core';
import { Nav, Platform } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';

import { TranslateService } from '@ngx-translate/core';
import { Globalization } from '@ionic-native/globalization';
import { TabsPage } from "../pages/tabs/tabs";
import { SignInPage } from "../pages/signIn/signIn";
import { Storage } from "@ionic/storage";
import { Config } from "ionic-angular";

@Component({
  templateUrl: 'app.html'
})

export class MyApp {
  @ViewChild(Nav) nav: Nav;
  // rootPage:any = SignInPage;

  constructor(
    platform: Platform,
    statusBar: StatusBar,
    splashScreen: SplashScreen,
    translate: TranslateService,
    private config: Config,
    private storage: Storage,
    private globalization: Globalization
  ) {

    this.storage.get('token').then((val) => {
      if (val) {
        this.nav.setRoot(TabsPage);
      }
      else {
        this.nav.setRoot(SignInPage);
      }
    });

    platform.ready().then(async () => {

      this.globalization.getPreferredLanguage()
        .then(res => console.log(res))
        .catch(e => console.log(e));

      if (platform.is('android')) {
        statusBar.backgroundColorByHexString('#FA671D');
      } else {
        statusBar.styleDefault();
      }
      splashScreen.hide();
      await translate.setDefaultLang('ru');
      translate.get('tabs.back').subscribe(backLabel => {
        this.config.set('backButtonText', backLabel);
      });
    });
  }
}
