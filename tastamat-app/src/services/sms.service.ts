import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

const baseUrl = "http://tasta.tastamat.com";

@Injectable ()
export class SmsService {
  constructor(private http: HttpClient) { }

  sendSms(phone): any {
    return this.http.put(`${baseUrl}/api/rest/auth/${phone}/sms`, {});
  }

  confirmCode(data): any {
    return this.http.put(`${baseUrl}/api/rest/auth/confirm`, data);
  }
}
