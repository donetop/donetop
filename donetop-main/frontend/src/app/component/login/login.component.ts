import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { DesktopComponent } from './desktop/desktop.component';
import { MobileComponent } from './mobile/mobile.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    DesktopComponent,
    MobileComponent
  ]
})
export class LoginComponent {

}
