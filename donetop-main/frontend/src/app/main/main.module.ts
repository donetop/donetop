import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainRoutingModule } from './main-routing.module';

import { MainComponent } from './main.component';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';

@NgModule({
  declarations: [
    MainComponent,
    HeaderComponent,
    FooterComponent,
  ],
  imports: [
    CommonModule,
    MainRoutingModule
  ],
  exports: [
    MainComponent
  ]
})
export class MainModule { }
