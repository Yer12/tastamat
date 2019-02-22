import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable ()
export class ProfileService {
  constructor(private http: HttpClient) {}

  getUserProfile(userId): any {
    return this.http.get(`https://testtasta.tastamat.com/api/rest/a/profiles/users/${userId}`);
  }

  changeSmsTemplate(data): any {
    return this.http.put('https://testtasta.tastamat.com/api/rest/a/profiles/template', data);
  }
}
