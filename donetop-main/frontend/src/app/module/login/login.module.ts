import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MobileModule } from './mobile/mobile.module';
import { DesktopModule } from './desktop/desktop.module';
import { LoginRoutingModule } from './login-routing.module';

import { LoginComponent } from './login.component';

@NgModule({
  declarations: [
    LoginComponent
  ],
  imports: [
    CommonModule,
    MobileModule,
    DesktopModule,
    LoginRoutingModule,
  ]
})
export class LoginModule {}
