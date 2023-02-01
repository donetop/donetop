import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Response } from '../http';

@Injectable({
  providedIn: 'root'
})
export class DraftService {

  private draftUri: string = '/api/draft';

  constructor(private httpClient: HttpClient, private router: Router) {}

  create(formData: FormData) {
    this.httpClient.post<Response<number>>(this.draftUri, formData)
      .subscribe({
        next: (response) => {
          console.log(`draft create success. draft id : ${response.data}`);
          this.router.navigateByUrl('/draft/list');
        },
        error: ({error}) => alert(error.reason)
      });
  }

}
