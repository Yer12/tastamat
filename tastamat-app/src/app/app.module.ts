import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { IonicStorageModule } from '@ionic/storage';
import { MyApp } from './app.component';

import { SignInPage } from '../pages/signIn/signIn';
import { EnterSmsCodePage } from "../pages/enterSmsCode/enterSmsCode";
import { TastamatsPage } from '../pages/tastamats/tastamats';
import { AddParcelPage } from '../pages/addParcel/addParcel';
import { OrdersPage } from '../pages/orders/orders';
import { ProfilePage } from '../pages/profile/profile';
import { SmsTemplatePage } from "../pages/smsTemplate/smsTemplate";
import { SetPasswordPage } from "../pages/setPasswordPage/setPassword";
import { QrScannerPage } from "../pages/qrScanner/qrScanner";
import { Verification_step1Page, Verification_step2Page } from "../pages/verification/verification";
import { TabsPage } from '../pages/tabs/tabs';

import { StatusBar } from '@ionic-native/status-bar';
import { File } from "@ionic-native/file";
import { SplashScreen } from '@ionic-native/splash-screen';
import { Globalization } from "@ionic-native/globalization";
import { Geolocation } from '@ionic-native/geolocation';
import { BarcodeScanner } from '@ionic-native/barcode-scanner';
import { InAppBrowser } from '@ionic-native/in-app-browser';
import { Camera } from '@ionic-native/camera';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { NgxMaskModule } from 'ngx-mask';
import { CountdownModule } from 'ngx-countdown';
import { SmsService } from "../services/sms.service";
import { AuthService } from "../services/auth.service";
import { OtherService } from "../services/other.service";
import { ProfileService } from "../services/profile.service";
import { PaymentService } from "../services/payment.service";
import { VerifyService } from "../services/verify.service";
import { InterceptorModule } from "../providers/interceptors/interceptor";
import { LoaderProvider } from "../providers/loader.provider";

export function setTranslateLoader(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, '../assets/i18n/', '.json');
}
// @ts-ignore
@NgModule({
  declarations: [
    MyApp,
    SignInPage,
    EnterSmsCodePage,
    AddParcelPage,
    OrdersPage,
    ProfilePage,
    SmsTemplatePage,
    SetPasswordPage,
    TastamatsPage,
    QrScannerPage,
    Verification_step1Page,
    Verification_step2Page,
    TabsPage
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(MyApp),
    HttpClientModule,
    InterceptorModule,
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
    OrdersPage,
    ProfilePage,
    SmsTemplatePage,
    TastamatsPage,
    QrScannerPage,
    Verification_step1Page,
    Verification_step2Page,
    TabsPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    Globalization,
    Geolocation,
    BarcodeScanner,
    InAppBrowser,
    Camera,
    File,
    SmsService,
    AuthService,
    OtherService,
    ProfileService,
    PaymentService,
    VerifyService,
    LoaderProvider,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
  ]
})
export class AppModule {}
