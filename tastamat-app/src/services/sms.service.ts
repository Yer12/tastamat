import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

const baseUrl = "https://testplatform.tastamat.com";

@Injectable ()
export class SmsService {
  constructor(private http: HttpClient) { }

  sendSms(phone): any {
    return this.http.put(`${baseUrl}/api/rest/auth/phones/${phone}/sms`, {});
  }

  confirmCode(data): any {
    return this.http.put(`${baseUrl}/api/rest/auth/phones/confirm`, data);
  }

  forgotPasswordStep2(data): any {
    return this.http.post(`${baseUrl}/api/rest/auth//forgotPasswordStep2`, data)
  }
}
