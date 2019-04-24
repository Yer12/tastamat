import { Component } from '@angular/core';
import { NavController, Platform } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { ProfileService } from "../../services/profile.service";
import { OtherService } from "../../services/other.service";

@Component({
  selector: 'smsTemplate-page',
  templateUrl: 'smsTemplate.html'
})
export class SmsTemplatePage {
  profile: {
    id: number;
    template: string;
  };
  template: string = '';

  constructor(
    public navCtrl: NavController,
    private storage: Storage,
    private profileService: ProfileService,
    private otherService: OtherService,
    public platform: Platform
  ) {
    this.platform.registerBackButtonAction(() => this.popView(),1);
  }

  ionViewWillEnter() {
    this.storage.get('profile').then(profile => {
      if (profile) {
        this.profile = profile;
        this.template = profile.template;
      }
    });
  }

  popView(){
    this.navCtrl.pop();
  }

  changeTemplate(event) {
    this.profile.template = event.target.value;
  }

  save() {
    this.profileService.changeSmsTemplate({id: this.profile.id, template: this.profile.template}).subscribe(
      res => {
        this.profile = res;
        this.popView();
      },
      err => this.otherService.handleError(err)
    )
  }

}
