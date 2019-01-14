import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { IonicStorageModule } from '@ionic/storage';
import { MyApp } from './app.component';

import { SignInPage } from '../pages/signIn/signIn';
import { EnterSmsCodePage } from "../pages/enterSmsCode/enterSmsCode";
import { TastamatsPage } from '../pages/tastamats/tastamats';
import { AddParcelPage } from '../pages/addParcel/addParcel';
import { ProfilePage } from '../pages/profile/profile';
import { SmsTemplatePage } from "../pages/smsTemplate/smsTemplate";
import { SetPasswordPage } from "../pages/setPasswordPage/setPassword";
import { QrScannerPage } from "../pages/qrScanner/qrScanner";
import { TabsPage } from '../pages/tabs/tabs';

import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { Globalization } from "@ionic-native/globalization";
import { Geolocation } from '@ionic-native/geolocation';
import { BarcodeScanner } from '@ionic-native/barcode-scanner';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { NgxMaskModule } from 'ngx-mask';
import { CountdownModule } from 'ngx-countdown';
import { SmsService } from "../services/sms.service";
import { AuthService } from "../services/auth.service";
import { OtherService } from "../services/other.service";
import { ProfileService } from "../services/profile.service";

export function setTranslateLoader(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, '../assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    MyApp,
    SignInPage,
    EnterSmsCodePage,
    AddParcelPage,
    ProfilePage,
    SmsTemplatePage,
    SetPasswordPage,
    TastamatsPage,
    QrScannerPage,
    TabsPage
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(MyApp),
    HttpClientModule,
    IonicStorageModule.forRoot(),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (setTranslateLoader),
        deps: [HttpClient]
      }
    }),
    NgxMaskModule.forRoot(),
    CountdownModule
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    SignInPage,
    EnterSmsCodePage,
    SetPasswordPage,
    AddParcelPage,
    ProfilePage,
    SmsTemplatePage,
    TastamatsPage,
    QrScannerPage,
    TabsPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    Globalization,
    Geolocation,
    BarcodeScanner,
    SmsService,
    AuthService,
    OtherService,
    ProfileService,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
  ]
})
export class AppModule {}
