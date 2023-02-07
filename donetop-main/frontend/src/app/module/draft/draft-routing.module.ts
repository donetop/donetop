import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DraftService } from 'src/app/service/draft.service';
import { CreateComponent } from './create/create.component';
import { DetailComponent } from './detail/detail.component';
import { ListComponent } from './list/list.component';

const routes: Routes = [
  {
    path: '',
    providers: [
      DraftService
    ],
    children: [
      { path: 'create', component: CreateComponent },
      { path: 'list', component: ListComponent },
      { path: 'detail', component: DetailComponent },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DraftRoutingModule { }
