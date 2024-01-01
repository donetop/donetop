import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { RouteName } from 'src/app/store/model/routeName.model';
import { Notice } from 'src/app/store/model/notice.model';
import { NoticeService } from 'src/app/service/notice.service';
import { Router } from '@angular/router';

declare const $: Function;

@Component({
  selector: 'app-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements AfterViewInit {

  routeName = RouteName.INSTANCE;
  notices: Array<Notice> = [];
  @ViewChild('file') file!: ElementRef;

  constructor(
    private noticeService: NoticeService, private router: Router
  ) {
    this.noticeService.notices()
      .subscribe({
        next: (response) => this.notices = response.data,
        error: ({error}) => { if ('reason' in error) alert(error.reason); }
      });
  }

  ngAfterViewInit() {
    $('#noticeCreateModal').on('hidden.bs.modal', function (e: object) {
      $('#noticeCreateModal').find('form').trigger('reset');
    })
  }

  createNotice(form: NgForm) {
    const fileList: FileList = this.file.nativeElement.files;
    if (fileList.length === 0) {
      alert(`파일을 선택해주세요.`);
      return;
    }

    const file: File = fileList[0];
    if (confirm('공지사항을 생성하시겠습니까?')) {
      const formData = new FormData();
      formData.append('title', form.controls['noticeTitle'].value)
      formData.append('files', file);
      this.noticeService.create(formData)
        .subscribe({
          next: (response) => {
            console.log(`notice create success. notice id : ${response.data}`);
            alert('공지사항 생성 성공');
            $('#noticeCreateModal').modal('hide');
            this.router.navigate([this.routeName.HOME])
              .then(() => this.router.navigate([this.routeName.NOTICE_LIST]));
          },
          error: ({error}) => alert(error.reason)
        });
    }
  }

  deleteNotice(id: number) {
    if (confirm(`공지사항을 삭제하시겠습니까?`)) {
      this.noticeService.delete(id)
        .subscribe({
          next: (response) => {
            alert('공지사항 삭제 성공');
            this.router.navigate([this.routeName.HOME])
              .then(() => this.router.navigate([this.routeName.NOTICE_LIST]));
          },
          error: ({error}) => alert(error.reason)
        });
    }
  }

}
