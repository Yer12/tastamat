import { Component } from '@angular/core';
import { LoadingController } from 'ionic-angular';
import { Geolocation } from '@ionic-native/geolocation';
import { OtherService } from "../../services/other.service";
import { Storage } from '@ionic/storage';

@Component({
  selector: 'page-tastamats',
  templateUrl: 'tastamats.html'
})
export class TastamatsPage {
  tastamats = {
    count: 0,
    list: []
  };
  lat: number;
  lng: number;
  page: number = 0;
  limit: number;
  showMore: boolean = false;

  constructor(
    private geolocation: Geolocation, private otherService: OtherService,
    private storage: Storage, public loadingCtrl: LoadingController
  ) {
    this.limit = screen.height
      ? Math.round((screen.height - 300)/64)
      : 5;
  }

  ionViewDidLoad() {
    this.getLocation(false);
  }

  getLocation(refresh) {
    let loader = this.loadingCtrl.create({ spinner: 'crescent' });
    loader.present();
    this.geolocation.getCurrentPosition({ timeout: 30000, enableHighAccuracy: true })
      .then((resp) => {
        this.lat = resp.coords.latitude;
        this.lng = resp.coords.longitude;
        this.getTastamats(resp.coords.latitude, resp.coords.longitude, refresh).then(
          () => loader.dismiss(),
          () => loader.dismiss()
        );
      })
      .catch((error) => {
        loader.dismiss();
        console.log('Error getting location', error);
      });
  }

  async doRefresh(refresher) {
    this.page = await 0;
    this.getLocation(true);
    setTimeout(() => {
      refresher.complete();
    }, 2000);
  }

  async getTastamats(lat, lng, refresh) {
    let promise = new Promise((resolve, reject) => {
      this.storage.get('token').then((token) => {
        if (token) {
          this.otherService.getTastamats(token, lat, lng, this.page, this.limit).subscribe(
            async response => {

              if (this.tastamats.list.length && !refresh)
                response.list.forEach(t => { this.tastamats.list.push(t); });
              else
                this.tastamats = await response;

              this.showMore = !(response.count === this.tastamats.list.length);
              resolve();
            },
            error => {
              console.log(error);
              this.tastamats.list = [];
              reject();
            }
          );
        }
      });
    });
  }

  async loadMore() {
    await this.page++;
    this.getLocation(false);
  }

  expand(item) {
    const button = document.getElementById(item.id);
    if (button.className.indexOf('expanded') > -1) {
      button.classList.remove('expanded');
    } else {
      const otherButtons = document.querySelectorAll('.tastamatApp__list__item');
      for (let i = 0; i < otherButtons.length; i++) {
        otherButtons[i].classList.remove('expanded');
      }
      button.classList.add('expanded');
    }
  }

}
