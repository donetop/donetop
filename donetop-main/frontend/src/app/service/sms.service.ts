import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Response } from "../store/model/response";

@Injectable({
  providedIn: 'root'
})
export class SmsService {
  private readonly ENDPOINT = '/api/auth/phone';

  constructor(private http: HttpClient) {}

  sendCode(phoneNumber: string) {
    return this.http.post<Response<string>>(`${this.ENDPOINT}/send`, { phoneNumber });
  }

  verifyCode(phoneNumber: string, code: string) {
    return this.http.post<Response<string>>(`${this.ENDPOINT}/verify`, { phoneNumber, code });
  }
}
