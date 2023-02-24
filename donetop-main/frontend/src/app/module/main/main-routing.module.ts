import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RouteName } from 'src/app/store/model/routeName.model';

import { HomeComponent } from 'src/app/component/home/home.component';

const INSTANCE = RouteName.INSTANCE;

const routes: Routes = [
  { path: INSTANCE.ROOT, redirectTo: INSTANCE.HOME, pathMatch: 'full' },
  { path: INSTANCE.HOME, component: HomeComponent },
  { path: INSTANCE.LOGIN, loadChildren: () => import("../login/login.module").then(m => m.LoginModule) },
  { path: INSTANCE.DRAFT, loadChildren: () => import("../draft/draft.module").then(m => m.DraftModule) },
  { path: INSTANCE.FALL_BACK, redirectTo: INSTANCE.HOME }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class MainRoutingModule { }
