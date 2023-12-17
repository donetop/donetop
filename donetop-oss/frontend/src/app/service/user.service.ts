import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Response } from '../store/model/response';
import { RouteName } from '../store/model/routeName.model';
import { User } from '../store/model/user.model';
import { Store } from '@ngrx/store';
import { UserUnloadAction } from '../store/action/user.action';
import { CryptoService } from './crypto.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private formLoginURI: string = '/api/form/login'
  private formLogoutURI: string = '/api/form/logout'
  private userURI: string = '/api/user'
  private routeName = RouteName.INSTANCE;

  constructor(
    private httpClient: HttpClient, private router: Router,
    private store: Store, private cryptoService: CryptoService
  ) {}

  login(form: NgForm) {
    const body = {
      'username': form.controls['username'].value,
      'password': this.cryptoService.encrypt(form.controls['password'].value)
    }
    this.httpClient.post<Response<string>>(this.formLoginURI, body)
    .subscribe({
      next: (response) => {
        console.log(`login success. user info : ${response.data}`);
        this.router.navigateByUrl(this.routeName.HOME);
      },
      error: ({error}) => alert(error.reason)
    });
  }

  logout() {
    this.httpClient.get<Response<string>>(this.formLogoutURI)
      .subscribe({
        next: (response) => {
          console.log(`logout success.`);
          this.store.dispatch(new UserUnloadAction());
          this.router.navigateByUrl(this.routeName.LOGIN);
        },
        error: ({error}) => alert(error.reason)
      });
  }

  getOSSUserInfo() {
    return this.httpClient.get<Response<User>>(this.userURI);
  }
}
