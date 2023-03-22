import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EntryRoutingModule } from './entry-routing.module';
import { NavigationEnd, Event, Router } from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { HttpClientModule } from '@angular/common/http';
import { RouteName } from 'src/app/store/model/routeName.model';

@Component({
  selector: 'app-entry',
  standalone: true,
  imports: [
    CommonModule,
    EntryRoutingModule,
    HttpClientModule,
    HeaderComponent,
    SidebarComponent
  ],
  providers: [
    { provide: RouteName, useValue: RouteName.INSTANCE },
  ],
  templateUrl: './entry.component.html',
  styleUrls: ['./entry.component.scss']
})
export class EntryComponent {

  isLogin: boolean = false;

  constructor(private router: Router) {
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        this.isLogin = event.urlAfterRedirects.includes('login');
      }
    });
  }

}
