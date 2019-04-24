import { Component } from '@angular/core';
import { Geolocation } from '@ionic-native/geolocation';
import { OtherService } from "../../services/other.service";
import { TranslateService } from "@ngx-translate/core";

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
  lang: string;

  constructor(
    private geolocation: Geolocation,
    private otherService: OtherService,
    private translateService: TranslateService
  ) {
    this.limit = screen.height
      ? Math.round((screen.height - 300)/64)
      : 5;
    this.lang = this.translateService.currentLang;
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
        this.getTastamats(null, null, true);
      });
  }

  async doRefresh(refresher) {
    this.page = await 0;
    this.getLocation(true);
    setTimeout(() => {
      refresher.complete();
    }, 1000);
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
      error => {
        this.tastamats.list = [];
        this.otherService.handleError(error);
      }
    );
  }

  async loadMore() {
    await this.page++;
    this.getTastamats(this.lat, this.lng, false);
  }

  expand(item) {
    const button = document.getElementById(item.id);
    button.classList.toggle('expanded');
  }

}
