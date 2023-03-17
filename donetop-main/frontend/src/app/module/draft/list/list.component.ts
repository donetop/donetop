import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
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
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';

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
  modalProperty: Property = Property.default();
  DRAFT_LIST: string = `/${this.routeName.DRAFT_LIST}`;
  isAdmin: boolean = false;
  params: any;
  searchConditions: Array<SearchCondition> = [SearchCondition.of('customerName', '고객명'), SearchCondition.of('phoneNumber', '전화번호')];
  searchKey: string = this.searchConditions[0].key;
  prevKey: string = this.searchKey;
  searchValue: string = '';

  constructor(
    private route: ActivatedRoute, private draftService: DraftService,
    protected routeName: RouteName, private router: Router,
    private store: Store<{ user: User }>, private cryptoService: CryptoService,
    private library: FaIconLibrary
  ) {
    this.route.queryParams.subscribe(params => this.setUp(params));
    this.store.select('user').subscribe(user => this.isAdmin = isAdmin(user));
    this.library.addIcons(faMagnifyingGlass);
  }

  setUp(params: any) {
    this.params = Object.assign({}, params);
    this.searchConditions.forEach(condidion => {
      const key = condidion.key;
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
}

class SearchCondition {
  constructor(public key: string, public name: string) {}
  static of(key: string, name: string) {
    return new SearchCondition(key, name);
  }
}
