import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable ()
export class OtherService {
  constructor(private http: HttpClient) {
  }

  getTastamats(token, lat, lng, page, limit): any {
    return this.http.get(
      `http://tasta.tastamat.com/api/rest/a/lockers?lat=${lat}&lng=${lng}&page=${page}&limit=${limit}`,
      { headers: new HttpHeaders({'Authorization': 'Bearer ' + token}) }
    )
  }

  openCell(token, data): any {
    return this.http.post('http://tasta.tastamat.com/api/rest/a/orders', data,{
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + token
      })
    })
  }

}
