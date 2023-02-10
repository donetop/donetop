import { Component } from '@angular/core';
import { RouteName } from 'src/app/store/model/routeName.model';

@Component({
  selector: 'app-mobile',
  templateUrl: './mobile.component.html',
  styleUrls: ['./mobile.component.scss']
})
export class MobileComponent {

  constructor(protected routeName: RouteName) {}

}
