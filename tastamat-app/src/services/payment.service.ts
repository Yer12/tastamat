import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

const baseUrl = "http://tasta.tastamat.com";

@Injectable ()
export class PaymentService {
  constructor(private http: HttpClient) {}

  getPaymentHistory(page, limit): any {
    return this.http.get(`${baseUrl}/api/rest/a/payment?page=${page}&limit=${limit}`)
  }

  getCurrentStatus(id): any {
    return this.http.put(`${baseUrl}/api/rest/a/payment/${id}/status`, '')
  }

  fillUpWallet(amount): any {
    return this.http.post(`${baseUrl}/api/rest/a/payment`, {
      amount: amount
    })
  }
}
