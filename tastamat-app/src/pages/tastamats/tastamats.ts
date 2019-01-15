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
  showMore: boolean = true;

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
    let options = {
      timeout: 30000,
      enableHighAccuracy: true
    };
    this.geolocation.getCurrentPosition(options)
      .then((resp) => {
        this.lat = resp.coords.latitude;
        this.lng = resp.coords.longitude;
        this.getTastamats(resp.coords.latitude, resp.coords.longitude, refresh);
      })
      .catch((error) => {
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
    this.storage.get('token').then((token) => {
      if (token) {
        let loader = this.loadingCtrl.create({ spinner: 'crescent' });
        loader.present();
        this.otherService.getTastamats(token, lat, lng, this.page, this.limit).subscribe(
          async response => {
            this.showMore = !(response.list.length < this.limit);

            if (this.tastamats.list.length && !refresh)
              response.list.forEach(t => { this.tastamats.list.push(t); });
            else
              this.tastamats = await response;

            loader.dismiss();
          },
          error => {
            console.log(error);
            this.tastamats.list = [];
            loader.dismiss();
          }
        );
      }
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
      const otherButtons = document.querySelectorAll('.tastamats__button');
      for (let i = 0; i < otherButtons.length; i++) {
        otherButtons[i].classList.remove('expanded');
      }
      button.classList.add('expanded');
    }
  }

}
