import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable ()
export class SmsService {
  constructor(private http: HttpClient) { }

  sendSms(phone): any {
    return this.http.put(`http://tasta.tastamat.com/api/rest/auth/${phone}/sms`, {});
  }

  confirmCode(data): any {
    return this.http.put('http://tasta.tastamat.com/api/rest/auth/confirm', data);
  }
}
