import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RouteName } from 'src/app/store/model/routeName.model';
import { HomeComponent } from '../home/home.component';
import { LoginComponent } from '../login/login.component';

const INSTANCE = RouteName.INSTANCE;

const routes: Routes = [
  { path: INSTANCE.ROOT, redirectTo: INSTANCE.LOGIN, pathMatch: 'full' },
  { path: INSTANCE.LOGIN, component: LoginComponent },
  { path: INSTANCE.HOME, component: HomeComponent },
  { path: INSTANCE.FALL_BACK, redirectTo: INSTANCE.HOME }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class EntryRoutingModule { }
