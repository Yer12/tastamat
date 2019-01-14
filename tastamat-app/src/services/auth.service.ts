import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable ()
export class AuthService {
  constructor(private http: HttpClient) { }

  checkNumber(phone): any {
    return this.http.get(`http://tasta.tastamat.com/api/rest/auth/7${phone}/exists`)
  }

  setPassword(data): any {
    return this.http.put('http://tasta.tastamat.com/api/rest/auth/password', data)
  }

  signIn(data): any {
    return this.http.put('http://tasta.tastamat.com/api/rest/auth/login', data);
  }
}
