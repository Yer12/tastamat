import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Storage } from '@ionic/storage';

@Injectable ()
export class ProfileService {
  token: string;
  constructor(private http: HttpClient, private storage: Storage) {
    this.storage.get('token').then((token) => {
      if (token) {
        this.token = token;
      }
      else {
        this.token = null
      }
    })
  }

  getUserProfile(userId): any {
    return this.http.get(`http://tasta.tastamat.com/api/rest/a/profiles/users/${userId}`, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + this.token
      })
    });
  }

  changeSmsTemplate(data): any {
    return this.http.put('http://tasta.tastamat.com/api/rest/a/profiles/template', data, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + this.token
      })
    });
  }
}
