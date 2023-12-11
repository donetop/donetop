import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Response } from '../store/model/response';

@Injectable({
  providedIn: 'root'
})
export class CustomerPostCommentService {

  private customerPostCommentURI = '/api/customerpost/comment';

  constructor(private httpClient: HttpClient) {}

  create(formData: FormData) {
    return this.httpClient.post<Response<number>>(this.customerPostCommentURI, formData);
  }

  delete(id: number) {
    return this.httpClient.delete<Response<number>>(`${this.customerPostCommentURI}/${id}`);
  }

}
