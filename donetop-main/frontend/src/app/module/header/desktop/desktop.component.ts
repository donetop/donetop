import { Component } from '@angular/core';
import { UserService } from 'src/app/service/user.service';
import { User } from 'src/app/store/model/user.model';

@Component({
  selector: 'app-desktop',
  templateUrl: './desktop.component.html',
  styleUrls: ['./desktop.component.scss']
})
export class DesktopComponent {
  public user: User | undefined;

  constructor(private userService: UserService) {
    this.userService.getUserInfo().subscribe({
      next: response => this.user = response.data,
      error: ({error}) => console.log(error.reason)
    });
  }

  logout() {
    this.userService.logout();
  }
}
