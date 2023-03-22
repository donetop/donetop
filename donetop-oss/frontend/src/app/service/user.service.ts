import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Response } from '../store/model/response';
import { RouteName } from '../store/model/routeName.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private formLoginURI: string = '/api/form/login'
  private formLogoutURI: string = '/api/form/logout'
  private routeName = RouteName.INSTANCE;

  constructor(private httpClient: HttpClient, private router: Router) {}

  login(form: NgForm) {
    this.httpClient.post<Response<string>>(this.formLoginURI, form.value)
    .subscribe({
      next: (response) => {
        console.log(`login success. user info : ${response.data}`);
        this.router.navigateByUrl(this.routeName.HOME);
      },
      error: ({error}) => alert(error.reason)
    });
  }
}
