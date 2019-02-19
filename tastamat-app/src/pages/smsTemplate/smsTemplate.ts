import { Component } from '@angular/core';
import { IonicPage, ViewController } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { ProfileService } from "../../services/profile.service";

@IonicPage()
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
    private view: ViewController,
    private storage: Storage,
    private profileService: ProfileService,
  ) {}

  ionViewWillEnter() {
    this.storage.get('profile').then(profile => {
      if (profile) {
        this.profile = profile;
        this.template = profile.template;
      }
    });
  }

  popView(){
    this.view.dismiss();
  }

  changeTemplate(event) {
    this.profile.template = event.target.value;
  }

  save() {
    this.profileService.changeSmsTemplate({id: this.profile.id, template: this.profile.template}).subscribe(
      res => this.profile = res,
      err => console.log(err)
    )
  }

}
