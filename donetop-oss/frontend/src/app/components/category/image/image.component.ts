import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoryService } from 'src/app/service/category.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Folder } from 'src/app/store/model/folder.model';
import { FormsModule } from '@angular/forms';
import { RouteName } from 'src/app/store/model/routeName.model';
import { File as DonetopFile } from 'src/app/store/model/file.model';

declare const $: any;

@Component({
  selector: 'app-image',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.scss']
})
export class ImageComponent implements AfterViewInit {

  routeName = RouteName.INSTANCE;
  params: any;
  folder: Folder | undefined = undefined;
  @ViewChild('file') file!: ElementRef;

  constructor(
    private categoryService: CategoryService, private route: ActivatedRoute,
    private router: Router
  ) {
    this.route.queryParams.subscribe(params => this.setUp(params));
  }

  ngAfterViewInit() {
    $('#imageAddModal').on('hidden.bs.modal', function (e: object) {
      $('#imageAddModal').find('form').trigger('reset');
    })
  }

  async setUp(params: any) {
    this.params = Object.assign({}, params);
    this.folder = (await this.categoryService.get(this.params['id'])).folder;
  }

  addImage() {
    const fileList: FileList = this.file.nativeElement.files;
    if (fileList.length === 0) {
      alert(`파일을 선택해주세요.`);
      return;
    }

    const file: File = fileList[0];
    if (confirm(`${file.name}을 추가하시겠습니까?`)) {
      const formData = new FormData();
      formData.append('files', file);
      this.categoryService.addImage(this.params['id'], formData)
        .subscribe({
          next: (response) => {
            alert('이미지 추가 성공');
            $('#imageAddModal').modal('hide');
            this.router.navigate([this.routeName.HOME])
              .then(() => this.router.navigate([this.routeName.CATEGORY_IMAGE], { queryParams: this.params }));
          },
          error: ({error}) => alert(error.reason)
        });
    }
  }

  deleteImage(file: DonetopFile) {
    if (confirm(`${file.name}을 삭제하시겠습니까?`)) {
      this.categoryService.deleteImage(this.params['id'], file.id)
        .subscribe({
          next: (response) => {
            alert('이미지 삭제 성공');
            this.router.navigate([this.routeName.HOME])
              .then(() => this.router.navigate([this.routeName.CATEGORY_IMAGE], { queryParams: this.params }));
          },
          error: ({error}) => alert(error.reason)
        });
    }
  }

}
