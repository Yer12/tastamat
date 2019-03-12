import {Component, ViewChild} from '@angular/core';
import {Nav, Platform} from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';

import { TranslateService } from '@ngx-translate/core';
import { Globalization } from '@ionic-native/globalization';
import { TabsPage } from "../pages/tabs/tabs";
import { SignInPage } from "../pages/signIn/signIn";
import { Storage } from "@ionic/storage";
import { Config } from "ionic-angular";
// import {Verification_step1Page, Verification_step3Page} from "../pages/verification/verification";
// import {AuthService} from "../services/auth.service";

@Component({
  templateUrl: 'app.html'
})

export class MyApp {
  @ViewChild(Nav) nav: Nav;

  constructor(
    platform: Platform,
    statusBar: StatusBar,
    splashScreen: SplashScreen,
    translate: TranslateService,
    private config: Config,
    private storage: Storage,
    private globalization: Globalization
    // private authService: AuthService
  ) {

    this.storage.get('token').then(token => {
      if (token) {
        this.nav.setRoot(TabsPage);
        // this.authService.getAccount().subscribe(
        //   res => {
        //     this.storage.set('user', res);
        //     console.log(res);
        //     if (!res.verified && !res.request)
        //       this.nav.setRoot(Verification_step1Page);
        //     if (!res.verified && res.request && (res.request.status === 'IN_PROCESS' || res.request.status === 'DECLINED'))
        //       this.nav.setRoot(Verification_step3Page);
        //     else
        //       this.nav.setRoot(TabsPage);
        //   },
        //   err => {
        //     this.storage.clear();
        //     this.nav.setRoot(SignInPage);
        //   }
        // );
      }
      else
        this.nav.setRoot(SignInPage);
    });

    platform.ready().then(async () => {

      this.globalization.getPreferredLanguage()
        .then(res => console.log(res))
        .catch(e => console.log(e));

      if (platform.is('android')) {
        statusBar.backgroundColorByHexString('#cc5418');
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
