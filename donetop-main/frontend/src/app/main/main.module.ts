import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainRoutingModule } from './main-routing.module';
import { HeaderModule } from '../header/header.module';

import { MainComponent } from './main.component';
import { FooterComponent } from '../footer/footer.component';

@NgModule({
  declarations: [
    MainComponent,
    FooterComponent
  ],
  imports: [
    CommonModule,
    MainRoutingModule,
    HeaderModule
  ],
  exports: [
    MainComponent
  ]
})
export class MainModule { }
