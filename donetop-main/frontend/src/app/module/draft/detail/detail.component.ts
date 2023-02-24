import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCreditCard } from '@fortawesome/free-regular-svg-icons';
import { faPenToSquare, faTrashCan } from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { TitleComponent } from 'src/app/component/title/title.component';
import { DraftService } from 'src/app/service/draft.service';
import { Draft } from 'src/app/store/model/draft.model';
import { OrderRequestParameter, OrderRequestParameterFrom } from 'src/app/store/model/nhn.model';
import { RouteName } from 'src/app/store/model/routeName.model';
import { isAdmin, User } from 'src/app/store/model/user.model';

declare const KCP_Pay_Execute: Function;

@Component({
  selector: 'app-detail',
  standalone: true,
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss'],
  imports: [
    CommonModule,
    TitleComponent,
    FontAwesomeModule
  ]
})
export class DetailComponent {

  draft: Draft | undefined;
  orderRequestParameter: OrderRequestParameter | undefined;
  @ViewChild('draft_order_form') draftOrderForm!: ElementRef;
  isAdmin: boolean = false;
  id: number = 0;
  password: string = '';

  constructor(
    private route: ActivatedRoute, private draftService: DraftService,
    private store: Store<{ user: User }>, private library: FaIconLibrary,
    private router: Router, private routeName: RouteName
  ) {
    this.library.addIcons(faTrashCan, faPenToSquare, faCreditCard);
    this.route.queryParams.subscribe(params => this.setUp(params));
    this.store.select('user').subscribe(user => this.isAdmin = isAdmin(user));
  }

  setUp(params: any) {
    this.id = parseInt(params['id']);
    this.password = params['p'];
    this.draftService.get(this.id, this.password)
      .subscribe({
        next: (response) => {
          this.draft = response.data;
          this.orderRequestParameter = OrderRequestParameterFrom(this.draft);
        },
        error: ({error}) => alert(error.reason)
      });
  }

  update() {
    this.router.navigate([this.routeName.DRAFT_UPDATE], { queryParams: { id: this.id, p: this.password } });
  }

  delete() {
    if (this.draft && confirm('정말로 삭제하시겠습니까?')) {
      this.draftService.delete(this.draft.id)
        .subscribe({
          next: (response) => this.router.navigate([this.routeName.DRAFT_LIST], { queryParams: { page: 0 } }),
          error: ({error}) => alert(error.reason)
        })
    }
  }

  pay() {
    KCP_Pay_Execute(this.draftOrderForm.nativeElement);
  }

  isPayable() {
    if (!this.draft) return false;
    return this.draft.price > 0;
  }

}
