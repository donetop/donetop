import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { MobileComponent } from '../mobile.component';
import { HomeComponent } from '../home/home.component';

const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
    // children: [
    //   { path: '', redirectTo: 'home', pathMatch: 'full' },
    //   { path: 'home', component: HomeComponent }
    // ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RoutingModule { }
