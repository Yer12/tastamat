import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

const baseUrl = "https://testtasta.tastamat.com";

@Injectable ()
export class AuthService {
  constructor(private http: HttpClient) { }

  checkNumber(phone): any {
    return this.http.get(`${baseUrl}/api/rest/auth/7${phone}/exists`)
  }

  setPassword(data): any {
    return this.http.put(`${baseUrl}/api/rest/auth/password`, data)
  }

  signIn(data): any {
    return this.http.put(`${baseUrl}/api/rest/auth/login`, data);
  }

  getAccount(): any {
    return this.http.get(`${baseUrl}/api/rest/a/users/current-user`)
  }
}
