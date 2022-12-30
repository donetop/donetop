import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { HeaderComponent } from './header.component';
import { CategoryComponent } from './category/category.component';
import { AccountComponent } from './account/account.component';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faBars, faUser, faAngleRight, faCaretRight, faAngleDown } from '@fortawesome/free-solid-svg-icons';

@NgModule({
  declarations: [
    HeaderComponent,
    CategoryComponent,
    AccountComponent
  ],
  imports: [
    CommonModule,
    FontAwesomeModule
  ],
  exports: [
    HeaderComponent
  ]
})
export class HeaderModule {
  constructor(library: FaIconLibrary) {
    library.addIcons(
      faBars, faUser, faAngleRight,
      faAngleDown, faCaretRight
    );
  }
}
