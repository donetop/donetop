import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TitleComponent } from '../../title/title.component';
import { Notice } from 'src/app/store/model/notice.model';
import { NoticeService } from 'src/app/service/notice.service';

@Component({
  selector: 'app-list',
  standalone: true,
  imports: [
    CommonModule,
    TitleComponent
  ],
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent {

  notices: Array<Notice> = [];

  constructor(
    private noticeService: NoticeService
  ) {
    this.noticeService.notices()
      .subscribe({
        next: (response) => this.notices = response.data,
        error: ({error}) => { if ('reason' in error) alert(error.reason); }
      });
  }

  toggle(divElement: HTMLDivElement, imgElement: HTMLImageElement) {
    divElement.classList.toggle('visible');
    imgElement.classList.toggle('visible');
  }

}
