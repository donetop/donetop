import { Routes } from "@angular/router";
import { RouteName } from "src/app/store/model/routeName.model";
import { ListComponent } from "./list/list.component";

const NOTICE_CHILDREN = RouteName.INSTANCE.NOTICE_CHILDREN;

export const routes: Routes = [
  {
    path: '',
    children: [
      { path: NOTICE_CHILDREN.LIST, component: ListComponent },
    ]
  }
];
