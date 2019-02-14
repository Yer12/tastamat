import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable ()
export class PaymentService {
  constructor(private http: HttpClient) {}

  getPaymentHistory(page, limit): any {
    return this.http.get(`http://tasta.tastamat.com/api/rest/a/payment?page=${page}&limit=${limit}`)
  }

  getCurrentStatus(id): any {
    return this.http.put(`http://tasta.tastamat.com/api/rest/a/payment/${id}/status`, '')
  }

  fillUpWallet(amount): any {
    return this.http.post(' http://tasta.tastamat.com/api/rest/a/payment', {
      amount: amount
    })
  }
}
