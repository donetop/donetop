import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Event, Router, RouterModule } from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { HttpClientModule } from '@angular/common/http';
import { FooterComponent } from '../footer/footer.component';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    HttpClientModule,
    HeaderComponent,
    SidebarComponent,
    FooterComponent
  ],
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent {

  isLogin: boolean = false;

  constructor(private router: Router) {
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        this.isLogin = event.urlAfterRedirects.includes('login');
      }
    });
  }

}
