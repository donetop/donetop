import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const breakpointDesktop = 1270;

const routes: Routes = [
  {
    path: '**',
    loadChildren: async () => {
      if(window.innerWidth >= breakpointDesktop) {
        console.log('load desktop...');
        return (await import('../mobile/mobile.module')).MobileModule;
      } else {
        console.log('load mobile...');
        return (await import('../mobile/mobile.module')).MobileModule;
      }
    }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class RoutingModule { }
