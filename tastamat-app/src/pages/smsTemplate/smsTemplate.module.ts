import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { SmsTemplatePage } from './smsTemplate';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import {HttpClient} from "@angular/common/http";

export function setTranslateLoader(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, '../assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    SmsTemplatePage,
  ],
  imports: [
    IonicPageModule.forChild(SmsTemplatePage),
    TranslateModule.forChild({
      loader: {
        provide: TranslateLoader,
        useFactory: (setTranslateLoader),
        deps: [HttpClient]
      }
    })
  ],
})
export class SmsTemplateModule {}
