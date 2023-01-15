import { AfterViewInit, Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { UserService } from 'src/app/service/user.service';
import { UserLoadAction } from 'src/app/store/action/user.action';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements AfterViewInit {

  constructor(private userService: UserService, private store: Store) {}

  ngAfterViewInit() {
    this.userService.getUserInfo().subscribe({
      next: response => this.store.dispatch(new UserLoadAction(response.data)),
      error: ({error}) => console.log(error.reason)
    });
  }

}
