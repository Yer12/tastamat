import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable ()
export class VerifyService {
  constructor(private http: HttpClient) {}

  sendForm(id, data) {
    return this.http.put(`https://testtasta.tastamat.com/api/rest/a/users/${id}/profile`, data);
  }

  uploadPhoto(id, data): any {
    return this.http.post(`https://testtasta.tastamat.com/api/rest/a/users/${id}/attachments`, data);
  }

  getPhoto(id): any {
    return this.http.get(`https://testtasta.tastamat.com/api/rest/a/files/${id}/view`);
  }

}
