import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import {HttpClient} from "@angular/common/http";
import { ManualInputPage } from './manualInput';

export function setTranslateLoader(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, '../assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    ManualInputPage,
  ],
  imports: [
    IonicPageModule.forChild(ManualInputPage),
    TranslateModule.forChild({
      loader: {
        provide: TranslateLoader,
        useFactory: (setTranslateLoader),
        deps: [HttpClient]
      }
    })
  ],
})
export class ManualInputPageModule {}
