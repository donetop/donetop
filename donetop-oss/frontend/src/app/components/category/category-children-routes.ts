import { Routes } from "@angular/router";
import { RouteName } from "src/app/store/model/routeName.model";
import { ListComponent } from "./list/list.component";

const CATEGORY_CHILDREN = RouteName.INSTANCE.CATEGORY_CHILDREN;

export const CATEGORY_CHILDREN_ROUTES: Routes = [
  {
    path: '',
    children: [
      { path: CATEGORY_CHILDREN.LIST, component: ListComponent }
    ]
  }
];
