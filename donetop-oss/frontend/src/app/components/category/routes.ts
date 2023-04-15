import { Routes } from "@angular/router";
import { RouteName } from "src/app/store/model/routeName.model";
import { ImageComponent } from "./image/image.component";
import { ListComponent } from "./list/list.component";

const CATEGORY_CHILDREN = RouteName.INSTANCE.CATEGORY_CHILDREN;

export const routes: Routes = [
  {
    path: '',
    children: [
      { path: CATEGORY_CHILDREN.LIST, component: ListComponent },
      { path: CATEGORY_CHILDREN.IMAGE, component: ImageComponent }
    ]
  }
];
