import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl } from "./other.service";

@Injectable ()
export class AuthService {
  constructor(private http: HttpClient) { }

  checkNumber(phone): any {
    return this.http.get(`${baseUrl}/auth/phones/7${phone}/exists`)
  }

  setPassword(data): any {
    return this.http.put(`${baseUrl}/auth/password`, data)
  }

  signIn(data): any {
    return this.http.put(`${baseUrl}/auth/login`, data);
  }

  getAccount(): any {
    return this.http.get(`${baseUrl}/a/users/current-user`)
  }

  initUser(phone): any {
    return this.http.post(`${baseUrl}/auth/initialize`, { phone: phone })
  }

}
