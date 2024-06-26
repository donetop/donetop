import { CommonModule } from '@angular/common';
import { Component, ElementRef, QueryList, ViewChildren } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { TitleComponent } from 'src/app/component/title/title.component';
import { InChargeNamePipe } from 'src/app/pipe/inchargename.pipe';
import { CryptoService } from 'src/app/service/crypto.service';
import { DraftService } from 'src/app/service/draft.service';
import { Draft } from 'src/app/store/model/draft.model';
import { Page } from 'src/app/store/model/page.model';
import { RouteName } from 'src/app/store/model/routeName.model';
import { User, isAdmin } from 'src/app/store/model/user.model';
import { ModalComponent, Property } from 'src/app/component/modal/modal.component';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCartShopping, faLock, faMagnifyingGlass, faTrashCan } from '@fortawesome/free-solid-svg-icons';
import { environment } from 'src/environments/environment.development';
import { SearchCondition } from 'src/app/store/model/common';

@Component({
  selector: 'app-list',
  standalone: true,
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
  imports: [
    CommonModule,
    RouterModule,
    InChargeNamePipe,
    TitleComponent,
    ModalComponent,
    FormsModule,
    FontAwesomeModule
  ]
})
export class ListComponent {

  page: Page<Draft> | undefined;
  pageNumber: number = 0;
  pageNumberSize: number = 10;
  pageNumbers: Array<number> = [];
  index: Array<number> = [];
  pageCount: number = 10;
  @ViewChildren('checkbox') checkboxes!: QueryList<ElementRef>;
  prevCheckedBoxIndex: number = -1;
  prevCheckedBoxStatus: boolean = true;
  modalProperty: Property = Property.default();
  routeName = RouteName.INSTANCE;
  isAdmin: boolean = false;
  params: any;
  searchConditions: Array<SearchCondition> = [SearchCondition.of('customerName', '고객명'), SearchCondition.of('phoneNumber', '전화번호')];
  searchKey: string = this.searchConditions[0].key;
  prevKey: string = this.searchKey;
  searchValue: string = '';

  constructor(
    private route: ActivatedRoute, private draftService: DraftService,
    private router: Router, private store: Store<{ user: User }>,
    private cryptoService: CryptoService, private library: FaIconLibrary
  ) {
    this.route.queryParams.subscribe(params => this.setUp(params));
    this.store.select('user').subscribe(user => this.isAdmin = isAdmin(user));
    this.library.addIcons(faMagnifyingGlass, faCartShopping, faLock, faTrashCan);
  }

  setUp(params: any) {
    this.params = Object.assign({}, params);
    this.searchConditions.forEach(condition => {
      const key = condition.key;
      if (key in this.params) {
        this.searchKey = key;
        this.searchValue = this.params[key];
        if (this.searchValue === '') {
          delete this.params[key];
          this.router.navigate([this.routeName.DRAFT_LIST], { queryParams: this.params });
        }
      }
    });
    this.prevKey = this.searchKey;
    this.pageNumber = parseInt(this.params['page']);
    this.draftService.list(this.params)
      .subscribe({
        next: (response) => {
          this.page = response.data;
          this.pageNumbers = new Array(this.page.totalPages)
            .fill(0)
            .map((v, i) => i)
            .filter(pageNumber => Math.floor(this.pageNumber / this.pageNumberSize) == Math.floor(pageNumber / this.pageNumberSize));
          const startIndex = this.page.totalElements - (this.pageNumber * this.page.size);
          this.index = new Array(this.page.numberOfElements).fill(0).map((v, i) => startIndex - i);
        },
        error: ({error}) => alert(error.reason)
      });
  }

  openModal(id: number, passwordForm: NgForm) {
    if (this.isAdmin) {
      this.router.navigate([this.routeName.DRAFT_DETAIL], { queryParams: { id, p: '' } });
    }
    this.modalProperty.toggleShow();
    this.modalProperty.resetFunc = () => passwordForm.resetForm();
    this.modalProperty.data = { id };
  }

  onSubmitModal(form: NgForm) {
    const id = this.modalProperty.data['id'];
    const password = this.cryptoService.encrypt(form.controls['password'].value);
    this.draftService.get(id, password)
      .subscribe({
        next: (response) => {
          this.router.navigate([this.routeName.DRAFT_DETAIL], { queryParams: { id, p: password } });
        },
        error: ({error}) => alert(error.reason)
      });
  }

  hasPrev() {
    return this.pageNumbers.length > 0 && this.pageNumbers[0] !== 0;
  }

  hasNext() {
    if (!this.page) return false;
    return this.pageNumbers.length > 0 && this.pageNumbers[this.pageNumbers.length - 1] !== this.page.totalPages - 1;
  }

  goToPage(page: number) {
    this.params['page'] = page;
    this.router.navigate([this.routeName.DRAFT_LIST], { queryParams: this.params });
  }

  search() {
    const newParams = Object.assign({}, this.params);
    if (this.prevKey !== this.searchKey) delete newParams[this.prevKey];
    newParams[this.searchKey] = this.searchValue;
    newParams['page'] = 0;
    this.router.navigate([this.routeName.DRAFT_LIST], { queryParams: newParams });
  }

  delete() {
    const elements = this.checkboxes.filter(element => element.nativeElement.checked);
    if (elements.length === 0) {
      alert('선택삭제할 항목을 선택하세요.');
      return;
    }
    if (confirm('정말 삭제하시겠습니까?')) {
      console.log(elements.map(e => e.nativeElement.id));
      this.draftService.deleteMultiple(elements.map(e => e.nativeElement.id))
        .subscribe({
          next: (response) => {
            console.log(`draft multiple delete success. deleted draft count : ${response.data}`);
            alert('삭제 성공');
            // 목록 업데이트들 위해 HOME으로 갔다가 돌아옴
            // router.routereusestrategy.shouldreuseroute를 변경할 수도 있으나 그렇게 하면 이후에 모든 다른 routerlink가 정상 동작 안함.
            // https://github.com/angular/angular/issues/13831
            this.router.navigate([this.routeName.HOME]).then(() => {
              this.goToPage(0);
            });
          },
          error: ({error}) => alert(error.reason)
        });
    }
  }

  goToOrderPage() {
    this.router.navigate([this.routeName.DRAFT_CREATE]);
  }

  checknthbox(index: number) {
    if (this.prevCheckedBoxIndex != -1 && index === this.prevCheckedBoxIndex) {
      const element = this.checkboxes.get(index)?.nativeElement;
      this.prevCheckedBoxStatus = !this.prevCheckedBoxStatus;
      element.checked = this.prevCheckedBoxStatus;
      return;
    }
    this.checkboxes.forEach((element, i) => element.nativeElement.checked = i === index);
    this.prevCheckedBoxIndex = index;
    this.prevCheckedBoxStatus = true;
  }

  isNew(draft: Draft) {
    let currentDate = new Date(new Date().toLocaleString('en', { timeZone: environment.timeZone }));
    let currentTime = Math.floor(currentDate.getTime() / 1000);
    let draftCreateTime = Math.floor(new Date(draft.createTime).getTime() / 1000);
    return Math.abs(currentTime - draftCreateTime) <= 1 * 60 * 60;
  }

  async checkComments(id: number) {
    await this.draftService.checkComments(id);
    this.draftService.get(id, '')
      .subscribe({
        next: (response) => {
          this.router.navigate([this.routeName.DRAFT_DETAIL], { queryParams: { id, p: '', focusOnComment: true } });
        },
        error: ({error}) => alert(error.reason)
      });
  }
}
