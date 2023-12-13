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

  private routeName = RouteName.INSTANCE;
  private draftURI: string = '/api/draft';
  private draftCopyURI: string = this.draftURI + '/copy';
  private draftsURI: string = '/api/drafts';

  constructor(private httpClient: HttpClient, private router: Router) {}

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

  list(params: object) {
    let requestURI = this.draftsURI;
    let first = true;
    for (const [key, value] of Object.entries(params)) {
      const symbol = first ? '?' : '&';
      requestURI += `${symbol}${key}=${value}`;
      if (first) first = false;
    }
    return this.httpClient.get<Response<Page<Draft>>>(requestURI);
  }

  get(id: number, password: string) {
    return this.httpClient.get<Response<Draft>>(`${this.draftURI}/${id}?password=${password}`);
  }

  delete(id: number) {
    return this.httpClient.delete<Response<number>>(`${this.draftURI}/${id}`);
  }

  copy(id: number) {
    return this.httpClient.post<Response<number>>(this.draftCopyURI, { id });
  }

}
