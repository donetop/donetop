import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MobileComponent } from './mobile.component';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    MobileComponent
  ],
  imports: [
    CommonModule,
    RouterModule
  ],
  exports: [
    MobileComponent
  ]
})
export class MobileModule { }
