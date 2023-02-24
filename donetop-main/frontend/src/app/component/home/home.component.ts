import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { DesktopComponent } from './desktop/desktop.component';
import { MobileComponent } from './mobile/mobile.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    DesktopComponent,
    MobileComponent
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {}
