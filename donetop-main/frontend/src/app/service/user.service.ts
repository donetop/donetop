import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Response } from '../http';
import { User } from '../store/model/user.model';
import { Store } from '@ngrx/store';
import { UserUnloadAction } from '../store/action/user.action';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private formLoginUri: string = '/api/form/login'
  private formLogoutUri: string = '/api/form/logout'
  private userUri: string = '/api/user'

  constructor(private httpClient: HttpClient, private router: Router, private store: Store) {}

  login(data: object) {
    this.httpClient.post<Response<string>>(this.formLoginUri, data)
      .subscribe({
        next: (response) => {
          console.log(`login success. user info : ${response.data}`);
          this.router.navigateByUrl('/');
        },
        error: ({error}) => alert(error.reason)
      });
  }

  logout() {
    this.httpClient.get<Response<string>>(this.formLogoutUri)
      .subscribe({
        next: (response) => {
          console.log(`logout success.`);
          this.store.dispatch(new UserUnloadAction());
        },
        error: ({error}) => alert(error.reason)
      });
  }

  getUserInfo() {
    return this.httpClient.get<Response<User>>(this.userUri);
  }
}
