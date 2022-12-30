import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderModule } from './header/header.module';
import { RoutingModule } from './routing/routing.module';
import { FooterModule } from './footer/footer.module';

import { MobileComponent } from './mobile.component';

@NgModule({
  declarations: [
    MobileComponent
  ],
  imports: [
    CommonModule,
    HeaderModule,
    RoutingModule,
    FooterModule
  ],
  exports: [
    MobileComponent
  ]
})
export class MobileModule { }
