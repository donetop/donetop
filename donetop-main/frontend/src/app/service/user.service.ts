import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Response } from '../store/model/response';
import { User } from '../store/model/user.model';
import { Store } from '@ngrx/store';
import { UserUnloadAction } from '../store/action/user.action';
import { NgForm } from '@angular/forms';
import { CryptoService } from './crypto.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private formLoginURI: string = '/api/form/login'
  private formLogoutURI: string = '/api/form/logout'
  private userURI: string = '/api/user'

  constructor(
    private httpClient: HttpClient, private router: Router,
    private store: Store, private cryptoService: CryptoService
  ) {}

  login(form: NgForm) {
    if (form.controls['username'].invalid) {
      alert("아이디를 입력해주세요.");
      document.getElementsByName('username').forEach(e => e.focus());
      return;
    }

    if (form.controls['password'].invalid) {
      alert("비밀번호 입력해주세요.");
      document.getElementsByName('password').forEach(e => e.focus());
      return;
    }

    const body = {
      'username': form.controls['username'].value,
      'password': this.cryptoService.encrypt(form.controls['password'].value),
      'autoLogin': form.controls['autoLogin'].value
    }

    this.httpClient.post<Response<string>>(this.formLoginURI, body)
      .subscribe({
        next: (response) => {
          console.log(`login success. user info : ${response.data}`);
          this.router.navigateByUrl('/');
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
          this.router.navigateByUrl('/');
        },
        error: ({error}) => alert(error.reason)
      });
  }

  getUserInfo() {
    return this.httpClient.get<Response<User>>(this.userURI);
  }
}
