import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Response } from '../http';
import { Store } from '@ngrx/store';
import { State } from '../store/model/state.model';
import { LoginAction, LogoutAction } from '../store/action/user.action';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private formLoginUri: string = '/api/form/login'
  private formLogoutUri: string = '/api/form/logout'

  constructor(private httpClient: HttpClient, private router: Router, private store: Store<State>) {}

  login(data: object) {
    this.httpClient.post<Response<string>>(this.formLoginUri, data)
      .subscribe({
        next: (response) => {
          console.log(`login success. user info : ${response.data}`);
          this.store.dispatch(new LoginAction({ name: response.data }));
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
          this.store.dispatch(new LogoutAction());
          this.router.navigateByUrl('/');
        },
        error: ({error}) => alert(error.reason)
      });
  }
}
