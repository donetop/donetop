import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Response } from '../store/model/response';
import { RouteName } from '../store/model/routeName.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  routeName = RouteName.INSTANCE;
  private commentURI: string = '/api/comment';

  constructor(private httpClient: HttpClient, private router: Router) {}

  create(formData: FormData) {
    return this.httpClient.post<Response<number>>(this.commentURI, formData);
  }

  delete(id: number) {
    return this.httpClient.delete<Response<number>>(`${this.commentURI}/${id}`);
  }

}
