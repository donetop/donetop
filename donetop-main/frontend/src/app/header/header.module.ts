import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MobileModule } from './mobile/mobile.module';
import { DesktopModule } from './desktop/desktop.module';

import { HeaderComponent } from './header.component';

@NgModule({
  declarations: [
    HeaderComponent,
  ],
  imports: [
    CommonModule,
    MobileModule,
    DesktopModule
  ],
  exports: [
    HeaderComponent
  ]
})
export class HeaderModule {}
