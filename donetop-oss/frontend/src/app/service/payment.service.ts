import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Response } from "../store/model/response";
import { Detail, PaymentInfo } from "../store/model/payment.model";
import { Page } from "../store/model/page.model";

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  private paymentURI: string = '/api/payment';
  private paymentsURI: string = '/api/payments';
  private cancelURI: string = `${this.paymentURI}/cancel`;

  constructor(private httpClient: HttpClient) {}

  list(params: object) {
    let requestURI = this.paymentsURI;
    let first = true;
    for (const [key, value] of Object.entries(params)) {
      const symbol = first ? '?' : '&';
      requestURI += `${symbol}${key}=${value}`;
      if (first) first = false;
    }
    return this.httpClient.get<Response<Page<PaymentInfo>>>(requestURI);
  }

  cancel(body: object) {
    return this.httpClient.post<Response<Detail>>(this.cancelURI, body);
  }

}
