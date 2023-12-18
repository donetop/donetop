import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Notice } from "../store/model/notice.model";
import { Response } from "../store/model/response";

@Injectable({
  providedIn: 'root'
})
export class NoticeService {

  private noticeURI = '/api/notice';
  private noticesURI = '/api/notices';

  constructor(private httpClient: HttpClient) {}

  notices() {
    return this.httpClient.get<Response<Array<Notice>>>(this.noticesURI);
  }

  create(formData: FormData) {
    return this.httpClient.post<Response<number>>(this.noticeURI, formData);
  }

  delete(id: number) {
    return this.httpClient.delete<Response<number>>(`${this.noticeURI}/${id}`);
  }

}
