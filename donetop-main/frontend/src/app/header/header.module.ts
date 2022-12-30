import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { HeaderComponent } from './header.component';
import { DesktopHeaderComponent } from '../header/desktop/desktop.header.component';
import { DesktopCategoryComponent } from './desktop/category/desktop.category.component';
import { MobileHeaderComponent } from '../header/mobile/mobile.header.component';
import { MobileCategoryComponent } from './mobile/category/mobile.category.component';
import { MobileAccountComponent } from './mobile/account/mobile.account.component';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faBars, faXmark, faUser, faAngleRight, faCaretRight, faAngleDown } from '@fortawesome/free-solid-svg-icons';

@NgModule({
  declarations: [
    HeaderComponent,
    DesktopHeaderComponent,
    DesktopCategoryComponent,
    MobileHeaderComponent,
    MobileCategoryComponent,
    MobileAccountComponent,
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
      faXmark,
      faUser,
      faAngleRight,
      faAngleDown,
      faCaretRight
    );
  }
}
