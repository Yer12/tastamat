import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl } from "./other.service";

@Injectable ()
export class VerifyService {
  constructor(private http: HttpClient) {}

  sendForm(id, data) {
    return this.http.put(`${baseUrl}/a/users/${id}/profile`, data);
  }

  uploadPhoto(id, data): any {
    return this.http.post(`${baseUrl}/a/users/${id}/attachments`, data);
  }

  deletePhoto(id): any {
    return this.http.delete(`${baseUrl}/a/files/${id}`)
  }

}
