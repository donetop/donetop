import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoaderService } from 'src/app/service/loader.service';

@Component({
  selector: 'app-spinner',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss']
})
export class SpinnerComponent {

  constructor(protected loaderService: LoaderService) {}

}
