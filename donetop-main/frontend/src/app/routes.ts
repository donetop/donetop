import { Routes } from '@angular/router';
import { RouteName } from 'src/app/store/model/routeName.model';

import { HomeComponent } from 'src/app/component/home/home.component';
import { CategoryComponent } from 'src/app/component/category/category.component';
import { IntroductionComponent } from 'src/app/component/introduction/introduction.component';
import { TermsComponent } from 'src/app/component/terms/terms.component';
import { PolicyComponent } from 'src/app/component/policy/policy.component';
import { UsageComponent } from './component/usage/usage.component';

const INSTANCE = RouteName.INSTANCE;

export const routes: Routes = [
  { path: INSTANCE.ROOT, redirectTo: INSTANCE.HOME, pathMatch: 'full' },
  { path: INSTANCE.HOME, component: HomeComponent },
  { path: INSTANCE.LOGIN, loadChildren: () => import("src/app/component/login/routes").then(result => result.routes) },
  { path: INSTANCE.DRAFT, loadChildren: () => import("src/app/component/draft/routes").then(result => result.routes) },
  { path: INSTANCE.CUSTOMERPOST, loadChildren: () => import("src/app/component/customerpost/routes").then(result => result.routes) },
  { path: INSTANCE.NOTICE, loadChildren: () => import("src/app/component/notice/routes").then(result => result.routes) },
  { path: INSTANCE.CATEGORY, component: CategoryComponent },
  { path: INSTANCE.INTRODUCTION, component: IntroductionComponent },
  { path: INSTANCE.TERMS, component: TermsComponent },
  { path: INSTANCE.POLICY, component: PolicyComponent },
  { path: INSTANCE.USAGE, component: UsageComponent },
  { path: INSTANCE.FALL_BACK, redirectTo: INSTANCE.HOME }
];
