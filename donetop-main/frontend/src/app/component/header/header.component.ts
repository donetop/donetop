import { CommonModule } from '@angular/common';
import { AfterViewInit, Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { UserService } from 'src/app/service/user.service';
import { UserLoadAction } from 'src/app/store/action/user.action';
import { DesktopComponent } from './desktop/desktop.component';
import { MobileComponent } from './mobile/mobile.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    DesktopComponent,
    MobileComponent
  ]
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
