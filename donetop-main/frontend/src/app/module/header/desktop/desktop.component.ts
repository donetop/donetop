import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { UserService } from 'src/app/service/user.service';
import { User } from 'src/app/store/model/user.model';

@Component({
  selector: 'app-desktop',
  templateUrl: './desktop.component.html',
  styleUrls: ['./desktop.component.scss']
})
export class DesktopComponent {
  public user: User | undefined;

  constructor(private store: Store<{ user: User }>, private userService: UserService) {
    store.select('user').subscribe(user => this.user = user);
  }

  logout() {
    this.userService.logout();
  }
}
