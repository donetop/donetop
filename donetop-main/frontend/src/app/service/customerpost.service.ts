import { Injectable } from "@angular/core";
import { RouteName } from "../store/model/routeName.model";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { CustomerPost } from "../store/model/customerpost.model";
import { Page } from "../store/model/page.model";
import { Response } from '../store/model/response';

@Injectable({
  providedIn: 'root'
})
export class CustomerPostService {

  private routeName = RouteName.INSTANCE
  private customerPostURI = '/api/customerpost';
  private customerPostsURI = '/api/customerposts';

  constructor(private httpClient: HttpClient, private router: Router) {}

  create(formData: FormData) {
    this.httpClient.post<Response<number>>(this.customerPostURI, formData)
      .subscribe({
        next: (response) => {
          console.log(`customer post create success. customer post id : ${response.data}`);
          this.router.navigate([this.routeName.CUSTOMERPOST_LIST], { queryParams: { page: 0 } });
        },
        error: ({error}) => alert(error.reason)
      });
  }

  list(params: object) {
    let requestURI = this.customerPostsURI;
    let first = true;
    for (const [key, value] of Object.entries(params)) {
      const symbol = first ? '?' : '&';
      requestURI += `${symbol}${key}=${value}`;
      if (first) first = false;
    }
    return this.httpClient.get<Response<Page<CustomerPost>>>(requestURI);
  }

  get(id: number) {
    return this.httpClient.get<Response<CustomerPost>>(`${this.customerPostURI}/${id}`);
  }

  delete(id: number) {
    return this.httpClient.delete<Response<number>>(`${this.customerPostURI}/${id}`);
  }

}
