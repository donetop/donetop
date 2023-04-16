import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faBars, faUser, faAngleRight, faCaretRight, faAngleDown } from '@fortawesome/free-solid-svg-icons';
import { RouteName } from 'src/app/store/model/routeName.model';
import { CategoryComponent } from './category/category.component';
import { AccountComponent } from './account/account.component';

@Component({
  selector: 'app-mobile',
  templateUrl: './mobile.component.html',
  styleUrls: ['./mobile.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FontAwesomeModule,
    CategoryComponent,
    AccountComponent
  ]
})
export class MobileComponent {

  routeName = RouteName.INSTANCE;

  constructor(library: FaIconLibrary) {
    library.addIcons(
      faBars, faUser, faAngleRight,
      faAngleDown, faCaretRight
    );
  }

}
