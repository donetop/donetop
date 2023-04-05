import { AfterViewInit, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoryService } from 'src/app/service/category.service';
import { Category } from 'src/app/store/model/category.model';
import { ActivatedRoute, Router } from '@angular/router';
import { RouteName } from 'src/app/store/model/routeName.model';
import { FormsModule, NgForm } from '@angular/forms';
import { CdkDragDrop, DragDropModule, moveItemInArray } from '@angular/cdk/drag-drop'

declare const $: any;

@Component({
  selector: 'app-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DragDropModule
  ],
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements AfterViewInit {

  routeName = RouteName.INSTANCE;
  params: any;
  categoryArray: Array<Category> = [];
  isSubCategoryList: boolean = false;

  constructor(
    private categoryService: CategoryService, private router: Router,
    private route: ActivatedRoute
  ) {
    this.route.queryParams.subscribe(params => this.setUp(params));
  }

  ngAfterViewInit() {
    $('#categoryCreateModal').on('hidden.bs.modal', function (e: object) {
      $('#categoryCreateModal').find('form').trigger('reset');
    })
  }

  async setUp(params: any) {
    this.params = Object.assign({}, params);
    const parentId = this.params['parentId'];
    this.isSubCategoryList = parentId !== undefined;
    this.categoryArray = parentId === undefined ? await this.categoryService.categoryArray() : await this.categoryService.subCategoryArray(parentId);
  }

  showSubCategory(categoryId: number) {
    this.params['parentId'] = categoryId;
    this.router.navigate([this.routeName.CATEGORY_LIST], { queryParams: this.params });
  }

  showImage(categoryId: number) {
    this.router.navigate([this.routeName.CATEGORY_IMAGE], { queryParams: { id: categoryId } });
  }

  createNewCategory(form: NgForm) {
    const parentId = this.params['parentId'];
    const data = {
      name: form.controls['categoryName'].value,
      parentCategoryId: parentId
    }
    this.categoryService.createNewCategory(data)
      .subscribe({
        next: (response) => {
          console.log(`category create success. category id : ${response.data}`);
          alert('카테고리 생성 성공');
          $('#categoryCreateModal').modal('hide');
          this.router.navigate([this.routeName.HOME])
            .then(() => this.router.navigate([this.routeName.CATEGORY_LIST], { queryParams: this.params }));
        },
        error: ({error}) => alert(error.reason)
      });
  }

  deleteCategory(categoryId: number) {
    if (confirm('정말 삭제하시겠습니까?\n서브 카테고리 및 이미지가 모두 삭제됩니다.')) {
      this.categoryService.delete(categoryId)
      .subscribe({
        next: (response) => {
          console.log(`category delete success. category id : ${response.data}`);
          alert('카테고리 삭제 성공');
          this.router.navigate([this.routeName.HOME])
            .then(() => this.router.navigate([this.routeName.CATEGORY_LIST], { queryParams: this.params }));
        },
        error: ({error}) => alert(error.reason)
      });
    }
  }

  drop(event: CdkDragDrop<Array<Category>>) {
    moveItemInArray(this.categoryArray, event.previousIndex, event.currentIndex);
  }

  isSorted() {
    return this.categoryArray.every((curr, i, array) => !i || array[i - 1].sequence < curr.sequence);
  }

  sortAndSave() {
    this.categoryArray.forEach((category, index) => category.sequence = index + 1);

    this.categoryService.sort(this.categoryArray)
      .subscribe({
        next: (response) => {
          console.log(`category sort success.`);
          alert('카테고리 정렬 성공');
        },
        error: ({error}) => alert(error.reason)
      });
  }

  numberOfImages(category: Category) {
    if (category.folder === undefined) return 0;
    return category.folder.files.length;
  }

}
