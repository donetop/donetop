import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MobileComponent } from './mobile.component';

@NgModule({
  declarations: [
    MobileComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    MobileComponent
  ]
})
export class MobileModule { }
