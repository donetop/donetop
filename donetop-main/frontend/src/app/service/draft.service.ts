import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Response } from '../http';
import { NgForm } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class DraftService {

  constructor(private httpClient: HttpClient, private router: Router) {}

  create(form: NgForm) {
    for (const id in form.controls) {
      const control = form.controls[id];
      if (control.invalid) {
        const element = document.getElementById(id);
        alert(element?.getAttribute('placeholder'));
        element?.focus();
        return;
      }
    }

    const formData = new FormData();
    console.log(form.value);
  }

}
