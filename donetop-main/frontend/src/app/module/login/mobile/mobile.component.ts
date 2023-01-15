import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-mobile',
  templateUrl: './mobile.component.html',
  styleUrls: ['./mobile.component.scss']
})
export class MobileComponent {

  constructor(private userService: UserService) {}

  onSubmit(form: NgForm) {
    if (form.controls['username'].invalid) {
      alert("아이디를 입력해주세요.");
      return;
    }

    if (form.controls['password'].invalid) {
      alert("비밀번호 입력해주세요.");
      return;
    }

    this.userService.login(form.value);
  }

}
