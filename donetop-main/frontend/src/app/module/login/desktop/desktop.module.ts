import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule }   from '@angular/forms';

import { DesktopComponent } from './desktop.component';

@NgModule({
  declarations: [
    DesktopComponent
  ],
  imports: [
    CommonModule,
    FormsModule
  ],
  exports: [
    DesktopComponent
  ]
})
export class DesktopModule { }
