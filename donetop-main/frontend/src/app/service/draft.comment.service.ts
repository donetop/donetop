import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Response } from '../store/model/response';
import { RouteName } from '../store/model/routeName.model';

@Injectable({
  providedIn: 'root'
})
export class DraftCommentService {

  routeName = RouteName.INSTANCE;
  private draftCommentURI: string = '/api/draft/comment';

  constructor(private httpClient: HttpClient, private router: Router) {}

  create(formData: FormData) {
    return this.httpClient.post<Response<number>>(this.draftCommentURI, formData);
  }

  delete(id: number) {
    return this.httpClient.delete<Response<number>>(`${this.draftCommentURI}/${id}`);
  }

}
