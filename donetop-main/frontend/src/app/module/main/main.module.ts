import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StoreModule } from '@ngrx/store';
import { HttpClientModule } from '@angular/common/http';
import { MainRoutingModule } from './main-routing.module';
import { HeaderModule } from '../header/header.module';

import { MainComponent } from './main.component';
import { FooterComponent } from '../footer/footer.component';

// import { userReducer } from '../../store/reducer/user.reducer';

@NgModule({
  declarations: [
    MainComponent,
    FooterComponent
  ],
  imports: [
    CommonModule,
    // StoreModule.forRoot({ user: userReducer }),
    HttpClientModule,
    MainRoutingModule,
    HeaderModule
  ],
  exports: [
    MainComponent
  ]
})
export class MainModule { }
