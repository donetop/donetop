import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, NavigationEnd, RouterModule, RouterEvent } from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { SpinnerComponent } from '../spinner/spinner.component';
import { LoaderService } from 'src/app/service/loader.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    HeaderComponent,
    FooterComponent,
    SpinnerComponent
  ]
})
export class MainComponent {

  isLogin: boolean = false;

  constructor(private router: Router, private loaderService: LoaderService) {
    this.router.events.subscribe((event: RouterEvent) => {
      if (event instanceof NavigationEnd) {
        this.isLogin = event.url.includes('login');
      }
      this.loaderService.acceptRouterEvent(event);
    });
  }
}
