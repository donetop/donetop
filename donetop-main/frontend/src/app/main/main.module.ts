import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainRoutingModule } from './main-routing.module';
import { FontAwesomeModule, FaIconLibrary } from '@fortawesome/angular-fontawesome';

import { MainComponent } from './main.component';
import { DesktopHeaderComponent } from '../header/desktop/desktop.header.component';
import { MobileHeaderComponent } from '../header/mobile/mobile.header.component';
import { FooterComponent } from '../footer/footer.component';

import { faBars } from '@fortawesome/free-solid-svg-icons';
import { faXmark } from '@fortawesome/free-solid-svg-icons';

@NgModule({
  declarations: [
    MainComponent,
    DesktopHeaderComponent,
    MobileHeaderComponent,
    FooterComponent,
  ],
  imports: [
    CommonModule,
    MainRoutingModule,
    FontAwesomeModule
  ],
  exports: [
    MainComponent
  ]
})
export class MainModule {
  constructor(library: FaIconLibrary) {
    library.addIcons(
      faBars,
      faXmark
    );
  }
}
