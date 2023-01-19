import { Component } from "@angular/core";
import { NgForm } from "@angular/forms";
import { UserService } from "src/app/service/user.service";

@Component({
  selector: 'app-base',
  template: ''
})
export class BaseComponent {

  autoLoginWarnMsg: string = `자동로그인을 사용하시면 다음부터 회원아이디와 비밀번호를 입력하실 필요가 없습니다.\n\n공공장소에서는 개인정보가 유출될 수 있으니 사용을 자제하여 주십시오.\n\n자동로그인을 사용하시겠습니까?`;

  constructor(private userService: UserService) {}

  onSubmit(form: NgForm) {
    this.userService.login(form);
  }

  onCheckboxChange(event: any) {
    if (event.target.checked) {
      event.target.checked = confirm(this.autoLoginWarnMsg);
    }
  }
}
