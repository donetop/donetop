import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule }   from '@angular/forms';

import { MobileComponent } from './mobile.component';

@NgModule({
  declarations: [
    MobileComponent
  ],
  imports: [
    CommonModule,
    FormsModule
  ],
  exports: [
    MobileComponent
  ]
})
export class MobileModule { }
