import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCog, faUser, faRightToBracket, faFileSignature, faCartShopping, faTruck } from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { UserService } from 'src/app/service/user.service';
import { RouteName } from 'src/app/store/model/routeName.model';
import { User, isAdmin } from 'src/app/store/model/user.model';
import { CategoryComponent } from './category/category.component';
import { environment } from 'src/environments/environment.development';

@Component({
  selector: 'app-desktop',
  templateUrl: './desktop.component.html',
  styleUrls: ['./desktop.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FontAwesomeModule,
    CategoryComponent
  ]
})
export class DesktopComponent {

  routeName = RouteName.INSTANCE;
  user: User | undefined;
  isAdmin: boolean = false;
  adminSiteURL: string = environment.adminSiteURL;

  constructor(
    private store: Store<{ user: User }>, private userService: UserService,
    private library: FaIconLibrary
  ) {
    this.store.select('user').subscribe(user => {
      this.user = user;
      this.isAdmin = isAdmin(this.user);
    });
    this.library.addIcons(
      faUser, faRightToBracket,
      faFileSignature, faCartShopping, faTruck,
      faCog
    );
  }

  logout() {
    this.userService.logout();
  }
}
