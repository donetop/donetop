import { Component, ElementRef, ViewChild } from '@angular/core';
import { Store } from '@ngrx/store';
import { UserService } from 'src/app/service/user.service';
import { RouteName } from 'src/app/store/model/routeName.model';
import { User } from 'src/app/store/model/user.model';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent {

  routeName = RouteName.INSTANCE;
  user: User | undefined;
  showSection: boolean = false;
  @ViewChild('left_section') leftSection!: ElementRef;
  @ViewChild('right_section') rightSection!: ElementRef;

  constructor(private store: Store<{ user: User }>, private userService: UserService) {
    this.store.select('user').subscribe(user => this.user = user);
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
}
