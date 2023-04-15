import { Routes } from '@angular/router';
import { RouteName } from 'src/app/store/model/routeName.model';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';

const INSTANCE = RouteName.INSTANCE;

export const routes: Routes = [
  { path: INSTANCE.ROOT, redirectTo: INSTANCE.LOGIN, pathMatch: 'full' },
  { path: INSTANCE.LOGIN, component: LoginComponent },
  { path: INSTANCE.HOME, component: HomeComponent },
  { path: INSTANCE.CATEGORY, loadChildren: () => import('./components/category/routes').then(result => result.routes) },
  { path: INSTANCE.FALL_BACK, redirectTo: INSTANCE.HOME }
];
