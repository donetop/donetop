import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCartShopping, faCog, faRightToBracket, faTruck } from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { UserService } from 'src/app/service/user.service';
import { RouteName } from 'src/app/store/model/routeName.model';
import { User, isAdmin } from 'src/app/store/model/user.model';
import { environment } from 'src/environments/environment.development';

declare const window: any;

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FontAwesomeModule
  ]
})
export class AccountComponent {

  routeName = RouteName.INSTANCE;
  user: User | undefined;
  isAdmin: boolean = false;
  adminSiteURL: string = environment.adminSiteURL;
  showSection: boolean = false;
  @ViewChild('left_section') leftSection!: ElementRef;
  @ViewChild('right_section') rightSection!: ElementRef;

  constructor(
    private store: Store<{ user: User }>, private userService: UserService,
    private library: FaIconLibrary
  ) {
    this.store.select('user').subscribe(user => {
      this.user = user;
      this.isAdmin = isAdmin(this.user);
    });
    this.library.addIcons(
      faRightToBracket, faCartShopping, faTruck,
      faCog
    );
  }

  logout() {
    this.userService.logout();
  }

  toggleSection() {
    this.showSection = !this.showSection;
    if (this.showSection) {
      this.leftSection.nativeElement.classList.add('active');
      this.rightSection.nativeElement.classList.add('active');
    } else {
      this.leftSection.nativeElement.classList.remove('active');
      this.rightSection.nativeElement.classList.remove('active');
    }
  }

  openAtBlank(url: string) {
    window.open(url, '_blank');
  }
}
