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
    FormsModule
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

  constructor(
    private route: ActivatedRoute, private draftService: DraftService,
    protected routeName: RouteName, private router: Router,
    private store: Store<{ user: User }>, private cryptoService: CryptoService
  ) {
    this.route.queryParams.subscribe(params => this.setUp(params));
    this.store.select('user').subscribe(user => this.isAdmin = isAdmin(user));
  }

  setUp(params: any) {
    this.pageNumber = parseInt(params['page']);
    this.draftService.list(this.pageNumber)
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

  forwardPage() {
    return this.pageNumbers[0] + this.pageCount;
  }

  backwardPage() {
    return this.pageNumbers[0] - 1;
  }
}
