import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

const baseUrl = "https://testtasta.tastamat.com";

@Injectable ()
export class VerifyService {
  constructor(private http: HttpClient) {}

  sendForm(id, data) {
    return this.http.put(`${baseUrl}/api/rest/a/users/${id}/profile`, data);
  }

  uploadPhoto(id, data): any {
    return this.http.post(`${baseUrl}/api/rest/a/users/${id}/attachments`, data);
  }

  deletePhoto(id): any {
    return this.http.delete(`${baseUrl}/api/rest/a/files/${id}`)
  }

}
