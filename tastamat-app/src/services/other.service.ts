import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable ()
export class OtherService {
  constructor(private http: HttpClient) {
  }

  getTastamats(token, lat, lng): any {
    return this.http.get(`http://tasta.tastamat.com/api/rest/a/lockers?lat=${lat}&lng=${lng}`, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + token
      })
    })
  }

  openCell(token, data): any {
    return this.http.post('http://tasta.tastamat.com/api/rest/a/orders', data,{
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + token
      })
    })
  }

}
