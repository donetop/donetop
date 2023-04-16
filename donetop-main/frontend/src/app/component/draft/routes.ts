import { Routes } from '@angular/router';
import { CreateComponent } from './create/create.component';
import { DetailComponent } from './detail/detail.component';
import { ListComponent } from './list/list.component';
import { UpdateComponent } from './update/update.component';
import { RouteName } from 'src/app/store/model/routeName.model';

const DRAFT_CHILDREN = RouteName.INSTANCE.DRAFT_CHILDREN;

export const routes: Routes = [
  {
    path: '',
    children: [
      { path: DRAFT_CHILDREN.CREATE, component: CreateComponent },
      { path: DRAFT_CHILDREN.LIST, component: ListComponent },
      { path: DRAFT_CHILDREN.DETAIL, component: DetailComponent },
      { path: DRAFT_CHILDREN.UPDATE, component: UpdateComponent },
    ]
  },
];
