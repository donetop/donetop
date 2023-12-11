import { Routes } from "@angular/router";
import { RouteName } from "src/app/store/model/routeName.model";
import { CreateComponent } from "./create/create.component";
import { ListComponent } from "./list/list.component";
import { DetailComponent } from "./detail/detail.component";

const CUSTOMERPOST_CHILDREN = RouteName.INSTANCE.CUSTOMERPOST_CHILDREN;

export const routes: Routes = [
  {
    path: '',
    children: [
      { path: CUSTOMERPOST_CHILDREN.CREATE, component: CreateComponent },
      { path: CUSTOMERPOST_CHILDREN.LIST, component: ListComponent },
      { path: CUSTOMERPOST_CHILDREN.DETAIL, component: DetailComponent }
    ]
  }
];
