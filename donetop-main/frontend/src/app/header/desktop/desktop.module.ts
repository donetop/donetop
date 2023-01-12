import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { DesktopComponent } from './desktop.component';
import { CategoryComponent } from './category/category.component';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faBars, faXmark, faUser, faRightToBracket, faFileSignature, faCartShopping, faTruck } from '@fortawesome/free-solid-svg-icons';

@NgModule({
  declarations: [
    DesktopComponent,
    CategoryComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    FontAwesomeModule
  ],
  exports: [
    DesktopComponent
  ]
})
export class DesktopModule {
  constructor(library: FaIconLibrary) {
    library.addIcons(
      faBars, faXmark, faUser, faRightToBracket,
      faFileSignature, faCartShopping, faTruck
    );
  }
}
