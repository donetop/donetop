import { AfterViewInit, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { RouteName } from 'src/app/store/model/routeName.model';
import { UserService } from 'src/app/service/user.service';
import { Store } from '@ngrx/store';
import { UserLoadAction } from 'src/app/store/action/user.action';
import { User } from 'src/app/store/model/user.model';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements AfterViewInit {

  routeName = RouteName.INSTANCE;
  user: User | undefined;

  constructor(
    protected userService: UserService, private store: Store,
    private userStore: Store<{ user: User }>, private router: Router
  ) {}

  ngAfterViewInit() {
    this.userService.getOSSUserInfo().subscribe({
      next: response => this.store.dispatch(new UserLoadAction(response.data)),
      error: ({error}) => {
        console.log(error.reason);
        this.router.navigateByUrl(this.routeName.LOGIN);
      }
    });
    this.userStore.select('user').subscribe(user => this.user = user);
  }

}
