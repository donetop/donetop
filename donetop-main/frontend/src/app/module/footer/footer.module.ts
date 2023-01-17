import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DesktopModule } from './desktop/desktop.module';
import { MobileModule } from './mobile/mobile.module';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { FooterComponent } from './footer.component';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faCircleArrowUp } from '@fortawesome/free-solid-svg-icons';

@NgModule({
  declarations: [
    FooterComponent,
  ],
  imports: [
    CommonModule,
    DesktopModule,
    MobileModule,
    FontAwesomeModule
  ],
  exports: [
    FooterComponent
  ]
})
export class FooterModule {
  constructor(library: FaIconLibrary) {
    library.addIcons(
      faCircleArrowUp
    );
  }
}
