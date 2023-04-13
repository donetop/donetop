import { Component } from '@angular/core';
import { RouteName } from 'src/app/store/model/routeName.model';

@Component({
  selector: 'app-desktop',
  templateUrl: './desktop.component.html',
  styleUrls: ['./desktop.component.scss']
})
export class DesktopComponent {
  routeName = RouteName.INSTANCE;
}

