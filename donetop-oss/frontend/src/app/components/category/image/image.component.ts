import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoryService } from 'src/app/service/category.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RouteName } from 'src/app/store/model/routeName.model';
import { File as DonetopFile } from 'src/app/store/model/file.model';
import { Category } from 'src/app/store/model/category.model';
import { StringAbbreviationPipe } from 'src/app/pipe/string-abbreviation.pipe';
import { CdkDragDrop, DragDropModule, moveItemInArray } from '@angular/cdk/drag-drop';
import { FileService } from 'src/app/service/file.service';

declare const $: Function;

@Component({
  selector: 'app-image',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DragDropModule,
    StringAbbreviationPipe
  ],
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.scss']
})
export class ImageComponent implements AfterViewInit {

  routeName = RouteName.INSTANCE;
  params: any;
  category!: Category;
  fileArray: Array<DonetopFile> = [];
  @ViewChild('file') file!: ElementRef;

  constructor(
    private categoryService: CategoryService, private route: ActivatedRoute,
    private router: Router, private fileService: FileService
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
    this.category = await this.categoryService.get(this.params['id']);
    if (this.category.folder !== undefined) this.fileArray = this.category.folder.files;
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

  drop(event: CdkDragDrop<Array<DonetopFile>>) {
    moveItemInArray(this.fileArray, event.previousIndex, event.currentIndex);
  }

  isSorted() {
    return this.fileArray.every((curr, i, array) => !i || array[i - 1].sequence < curr.sequence);
  }

  sortAndSave() {
    this.fileArray.forEach((file, index) => file.sequence = index + 1);

    this.fileService.sort(this.fileArray)
      .subscribe({
        next: (response) => {
          console.log(`file sort success.`);
          alert('파일 정렬 성공');
        },
        error: ({error}) => alert(error.reason)
      });
  }

}
