import { Routes } from "@angular/router";
import { RouteName } from "src/app/store/model/routeName.model";
import { ListComponent } from "./list/list.component";

const PAYMENT_CHILDREN = RouteName.INSTANCE.PAYMENT_CHILDREN;

export const routes: Routes = [
  {
    path: '',
    children: [
      { path: PAYMENT_CHILDREN.LIST, component: ListComponent }
    ]
  }
];
