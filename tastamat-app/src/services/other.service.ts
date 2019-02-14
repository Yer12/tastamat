import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable ()
export class OtherService {
  constructor(private http: HttpClient) {}

  getTastamats(lat, lng, page, limit): any {
    return this.http.get(
      `http://tasta.tastamat.com/api/rest/a/lockers?lat=${lat}&lng=${lng}&page=${page}&limit=${limit}`
    )
  }

  openCell(data): any {
    return this.http.post('http://tasta.tastamat.com/api/rest/a/orders', data)
  }

  getOrders(status, page, limit): any {
    return this.http.get(`http://tasta.tastamat.com/api/rest/a/orders?status=${status}&page=${page}&limit=${limit}`)
  }

  withdrawFromCell(data): any {
    return this.http.put('http://tasta.tastamat.com/api/rest/a/orders/withdraw', data)
  }

}
