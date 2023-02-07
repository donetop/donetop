import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Response } from '../store/model/response';
import { Page } from '../store/model/page.model';
import { Draft } from '../store/model/draft.model';

@Injectable({
  providedIn: 'root'
})
export class DraftService {

  private draftUri: string = '/api/draft';
  private draftsUri: string = '/api/drafts';

  constructor(private httpClient: HttpClient, private router: Router) {}

  create(formData: FormData) {
    this.httpClient.post<Response<number>>(this.draftUri, formData)
      .subscribe({
        next: (response) => {
          console.log(`draft create success. draft id : ${response.data}`);
          this.router.navigate(['/draft/list'], { queryParams: { page: 0 } });
        },
        error: ({error}) => alert(error.reason)
      });
  }

  list(page: number) {
    return this.httpClient.get<Response<Page<Draft>>>(`${this.draftsUri}?page=${page}`);
  }

  get(id: number, password: string) {
    return this.httpClient.get<Response<Draft>>(`${this.draftUri}/${id}?password=${password}`);
  }

}
