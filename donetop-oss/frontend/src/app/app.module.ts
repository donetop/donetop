import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { StoreModule } from '@ngrx/store';

import { AppComponent } from './app.component';
import { EntryComponent } from './components/entry/entry.component';
import { ossUserReducer } from './store/reducer/oss-user.reducer';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    EntryComponent,
    StoreModule.forRoot({ ossUser: ossUserReducer })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
