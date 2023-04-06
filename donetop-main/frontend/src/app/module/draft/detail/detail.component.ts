import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCopy, faCreditCard } from '@fortawesome/free-regular-svg-icons';
import { faPenToSquare, faReceipt, faTrashCan } from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { ModalComponent, Property } from 'src/app/component/modal/modal.component';
import { TitleComponent } from 'src/app/component/title/title.component';
import { DraftService } from 'src/app/service/draft.service';
import { Draft } from 'src/app/store/model/draft.model';
import { OrderRequest, OrderRequestFromDraft, TradeRegisterRequestFromDraft, TradeRegisterRequest } from 'src/app/store/model/nhn.model';
import { PaymentHistory } from 'src/app/store/model/payment.model';
import { RouteName } from 'src/app/store/model/routeName.model';
import { isAdmin, User } from 'src/app/store/model/user.model';
import jsPDF from 'jspdf';
import { malgun } from './font';

declare const call_pay_form: Function;
declare const KCP_Pay_Execute: Function;
declare const trade_register: Function;

@Component({
  selector: 'app-detail',
  standalone: true,
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss'],
  imports: [
    CommonModule,
    FontAwesomeModule,
    TitleComponent,
    ModalComponent
  ]
})
export class DetailComponent {

  draft: Draft | undefined;
  orderRequest: OrderRequest | undefined;
  tradeRegisterRequest: TradeRegisterRequest | undefined;
  @ViewChild('pc_order_form') pcOrderForm!: ElementRef;
  @ViewChild('mobile_order_form') mobileOrderForm!: ElementRef;
  isAdmin: boolean = false;
  id: number = 0;
  password: string = '';
  modalProperty: Property = Property.default();
  modalObject = {
    '주문번호': 'order_no',
    '거래번호': 'tno',
    '카드명': 'card_name',
    '카드번호': 'card_no',
    '가격(원)': 'amount',
    '상태': 'res_msg'
  }
  paymentHistory: PaymentHistory | undefined;
  isMobile = /iPhone|iPad|iPod|Android/i.test(window.navigator.userAgent);
  mobile_opt_param1 = `${location.pathname}${location.search}`;
  private routeName = RouteName.INSTANCE;

  constructor(
    private route: ActivatedRoute, private draftService: DraftService,
    private store: Store<{ user: User }>, private library: FaIconLibrary,
    private router: Router
  ) {
    this.library.addIcons(faTrashCan, faPenToSquare, faCreditCard, faReceipt, faCopy);
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
          this.paymentHistory = this.draft.paymentInfo?.lastHistory;
          this.orderRequest = OrderRequestFromDraft(this.draft);
          this.tradeRegisterRequest = TradeRegisterRequestFromDraft(this.draft);
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

  async pay() {
    if (this.isMobile) {
      const response = await trade_register(this.tradeRegisterRequest);
      if (response.code === 200) {
        const form = this.mobileOrderForm.nativeElement;
        form.elements['approval_key'].value = response.data.approvalKey;
        form.elements['traceNo'].value = response.data.traceNo;
        form.elements['PayUrl'].value = response.data.PayUrl;
        call_pay_form();
      }
    } else {
      KCP_Pay_Execute(this.pcOrderForm.nativeElement);
    }
  }

  isPayable() {
    if (!this.draft) return false;
    if (this.draft.price <= 0) return false;
    if (this.draft.paymentMethod.name !== 'CREDIT_CARD') return false;
    if (this.draft.paymentInfo && this.draft.paymentInfo.paymentStatus === 'PAID') return false;
    return true;
  }

  isPaid() {
    if (!this.draft) return false;
    return this.draft.paymentInfo && this.draft.paymentInfo.paymentStatus === 'PAID';
  }

  openModal() {
    this.modalProperty.toggleShow();
  }

  closeModal() {
    this.modalProperty.toggleShow();
  }

  print() {
    if (!this.paymentHistory) return;
    const doc = new jsPDF("l", "mm", "a4");
    doc.addFileToVFS('malgun-normal.ttf', malgun);
    doc.addFont('malgun-normal.ttf', 'malgun', 'normal');
    doc.setFont('malgun');

    let x = 20, y = 20, xGap = 40, yGap = 10;

    doc.addImage("assets/header/logo.png", "PNG", x + 1, y, 30, 30);
    y += (yGap * 5);

    doc.setFontSize(18);
    doc.text("영수증", x, y);
    y += 5;

    doc.setFontSize(16);
    y += yGap;
    doc.text("고객명", x, y);
    doc.text(':', x + xGap - 10, y);
    doc.text(`${this.draft?.customerName}`, x + xGap, y);
    for (const [key, value] of Object.entries(this.modalObject)) {
      y += yGap;
      doc.text(key, x, y);
      doc.text(':', x + xGap - 10, y);
      doc.text(`${this.paymentHistory.detail[value]}`, x + xGap, y);
    }
    y += yGap;
    doc.text("거래날짜", x, y);
    doc.text(':', x + xGap - 10, y);
    doc.text(`${this.paymentHistory.createTime}`, x + xGap, y);

    doc.autoPrint({ variant: 'non-conform' });
    doc.save(`donetop-${this.paymentHistory.detail[this.modalObject['거래번호']]}`);
  }

  copy() {
    if (!this.draft) return;
    if (confirm('정말 복사하시겠습니까?')) {
      this.draftService.copy(this.draft.id)
        .subscribe({
          next: (response) => {
            console.log(`draft copy success. copied draft id : ${response.data}`);
            alert('복사 성공');
            this.router.navigate([this.routeName.DRAFT_LIST], { queryParams: { page: 0 } });
          },
          error: ({error}) => alert(error.reason)
        });
    }
  }

}
