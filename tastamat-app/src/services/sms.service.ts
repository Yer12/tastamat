import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

const baseUrl = "https://platform.tastamat.com/platform/v1/rest";

@Injectable ()
export class SmsService {
  constructor(private http: HttpClient) { }

  sendSms(phone): any {
    return this.http.put(`${baseUrl}/auth/phones/${phone}/sms`, {});
  }

  confirmCode(data): any {
    return this.http.put(`${baseUrl}/auth/phones/confirm`, data);
  }

}
