import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

const baseUrl = "https://platform.tastamat.com/platform/v1/rest";

@Injectable ()
export class PaymentService {
  constructor(private http: HttpClient) {}

  getPaymentHistory(page, limit): any {
    return this.http.get(`${baseUrl}/a/payments?page=${page}&limit=${limit}`)
  }

  getCurrentStatus(id): any {
    return this.http.put(`${baseUrl}/a/payments/${id}/status`, '')
  }

  fillUpWallet(amount): any {
    return this.http.post(`${baseUrl}/a/payments`, {
      amount: amount
    })
  }

  successPayment(id): any {
    return this.http.put(`${baseUrl}/a/payments/${id}/succeeded`, '')
  }
}
