import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Response } from '../store/model/response';
import { Page } from '../store/model/page.model';
import { Draft } from '../store/model/draft.model';
import { RouteName } from '../store/model/routeName.model';

@Injectable({
  providedIn: 'root'
})
export class DraftService {

  private draftURI: string = '/api/draft';
  private draftsURI: string = '/api/drafts';

  constructor(private httpClient: HttpClient, private router: Router, private routeName: RouteName) {}

  create(formData: FormData) {
    this.httpClient.post<Response<number>>(this.draftURI, formData)
      .subscribe({
        next: (response) => {
          console.log(`draft create success. draft id : ${response.data}`);
          this.router.navigate([this.routeName.DRAFT_LIST], { queryParams: { page: 0 } });
        },
        error: ({error}) => alert(error.reason)
      });
  }

  update(id: number, password: string, formData: FormData) {
    this.httpClient.put<Response<number>>(`${this.draftURI}/${id}`, formData)
      .subscribe({
        next: (response) => {
          console.log(`draft update success. draft id : ${response.data}`);
          this.router.navigate([this.routeName.DRAFT_DETAIL], { queryParams: { id, p: password } });
        },
        error: ({error}) => alert(error.reason)
      });
  }

  list(page: number) {
    return this.httpClient.get<Response<Page<Draft>>>(`${this.draftsURI}?page=${page}`);
  }

  get(id: number, password: string) {
    return this.httpClient.get<Response<Draft>>(`${this.draftURI}/${id}?password=${password}`);
  }

  delete(id: number) {
    return this.httpClient.delete<Response<number>>(`${this.draftURI}/${id}`);
  }

}
