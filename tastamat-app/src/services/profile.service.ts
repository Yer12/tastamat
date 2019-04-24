import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl } from "./other.service";

@Injectable ()
export class ProfileService {
  constructor(private http: HttpClient) {}

  getUserProfile(userId): any {
    return this.http.get(`${baseUrl}/a/profiles/users/${userId}`);
  }

  changeSmsTemplate(data): any {
    return this.http.put(`${baseUrl}/a/profiles/template`, data);
  }
}
