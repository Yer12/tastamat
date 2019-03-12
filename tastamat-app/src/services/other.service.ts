import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

const baseUrl = "http://tasta.tastamat.com";

@Injectable ()
export class OtherService {
  constructor(private http: HttpClient) {}

  getTastamats(lat, lng, page, limit): any {
    return this.http.get(
      `${baseUrl}/api/rest/a/lockers?lat=${lat}&lng=${lng}&page=${page}&limit=${limit}`
    )
  }

  openCell(data): any {
    return this.http.post(`${baseUrl}/api/rest/a/orders`, data)
  }

  getOrders(status, page, limit): any {
    return this.http.get(`${baseUrl}/api/rest/a/orders?status=${status}&page=${page}&limit=${limit}`)
  }

  withdrawFromCell(data): any {
    return this.http.put(`${baseUrl}/api/rest/a/orders/withdraw`, data)
  }

}
