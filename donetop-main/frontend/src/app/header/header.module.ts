import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { HeaderComponent } from './header.component';
import { DesktopHeaderComponent } from '../header/desktop/desktop.header.component';
import { MobileHeaderComponent } from '../header/mobile/mobile.header.component';
import { CategoryComponent } from './desktop/category/category.component';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { faXmark } from '@fortawesome/free-solid-svg-icons';

@NgModule({
  declarations: [
    HeaderComponent,
    DesktopHeaderComponent,
    MobileHeaderComponent,
    CategoryComponent
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
      faBars,
      faXmark
    );
  }
}
