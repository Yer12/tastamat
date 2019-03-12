import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

const baseUrl = "https://testtasta.tastamat.com";

@Injectable ()
export class ProfileService {
  constructor(private http: HttpClient) {}

  getUserProfile(userId): any {
    return this.http.get(`${baseUrl}/api/rest/a/profiles/users/${userId}`);
  }

  changeSmsTemplate(data): any {
    return this.http.put(`${baseUrl}/api/rest/a/profiles/template`, data);
  }
}
