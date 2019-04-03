import { Component } from '@angular/core';
import { Geolocation } from '@ionic-native/geolocation';
import { OtherService } from "../../services/other.service";

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
  searchText: string;

  constructor(private geolocation: Geolocation, private otherService: OtherService) {
    this.limit = screen.height
      ? Math.round((screen.height - 300)/64)
      : 5;
  }

  ionViewDidLoad() {
    this.getLocation(true);
  }

  searchBy() {
    this.getTastamats(this.lat, this.lng, true);
  }

  getLocation(refresh) {
    this.geolocation.getCurrentPosition({ timeout: 3000, enableHighAccuracy: true })
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
    this.otherService.getTastamats(lat, lng, this.page, this.limit, this.searchText).subscribe(
      async response => {

        if (this.tastamats.list.length && !refresh)
          response.list.forEach(t => { this.tastamats.list.push(t); });
        else
          this.tastamats = await response;

        this.showMore = !(response.count === this.tastamats.list.length);
      },
      error => this.tastamats.list = []
    );
  }

  async loadMore() {
    await this.page++;
    this.getTastamats(this.lat, this.lng, false);
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
