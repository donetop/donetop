import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StoreModule } from '@ngrx/store';
import { HttpClientModule } from '@angular/common/http';
import { MainRoutingModule } from './main-routing.module';
import { HeaderModule } from '../header/header.module';
import { FooterModule } from '../footer/footer.module';

import { MainComponent } from './main.component';

import { userReducer } from '../../store/reducer/user.reducer';

@NgModule({
  declarations: [
    MainComponent
  ],
  imports: [
    CommonModule,
    StoreModule.forRoot({ user: userReducer }),
    HttpClientModule,
    MainRoutingModule,
    HeaderModule,
    FooterModule
  ],
  exports: [
    MainComponent
  ]
})
export class MainModule { }
