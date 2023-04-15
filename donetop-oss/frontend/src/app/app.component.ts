import { Component } from '@angular/core';
import { MainComponent } from './components/main/main.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  standalone: true,
  imports: [
    MainComponent
  ]
})
export class AppComponent {}
