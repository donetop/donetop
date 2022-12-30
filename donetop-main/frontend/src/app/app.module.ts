import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
// import { MainModule } from './main/main.module';
import { RoutingModule } from './routing/routing.module';

import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    // MainModule,
    RoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
