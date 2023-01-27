import { Component } from '@angular/core';
import { TitleComponent } from 'src/app/component/title/title.component';

@Component({
  selector: 'app-list',
  standalone: true,
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
  imports: [TitleComponent]
})
export class ListComponent {

}
