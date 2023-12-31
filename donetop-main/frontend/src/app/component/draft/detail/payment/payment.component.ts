import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Draft } from 'src/app/store/model/draft.model';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { OrderRequest, OrderRequestFromDraft, TradeRegisterRequest, TradeRegisterRequestFromDraft } from 'src/app/store/model/nhn.model';
import { faCreditCard } from '@fortawesome/free-regular-svg-icons';
import { faReceipt } from '@fortawesome/free-solid-svg-icons';
import { ModalComponent, Property } from 'src/app/component/modal/modal.component';
import { PaymentHistory } from 'src/app/store/model/payment.model';
import { jsPDF } from 'jspdf';
import { malgun } from './font';

declare const call_pay_form: Function;
declare const KCP_Pay_Execute: Function;
declare const trade_register: Function;

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [
    CommonModule,
    FontAwesomeModule,
    ModalComponent
  ],
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss']
})
export class PaymentComponent {

  _draft: Draft | undefined;
  isMobile = /iPhone|iPad|iPod|Android/i.test(window.navigator.userAgent);
  orderRequest: OrderRequest | undefined;
  tradeRegisterRequest: TradeRegisterRequest | undefined;
  lastPaymentHistory: PaymentHistory | undefined;
  @ViewChild('pc_order_form') pcOrderForm!: ElementRef;
  @ViewChild('mobile_order_form') mobileOrderForm!: ElementRef;
  mobile_opt_param1 = `${location.pathname}${location.search}`;
  modalProperty: Property = Property.default();
  modalObject = {
    '주문번호': 'order_no',
    '거래번호': 'tno',
    '카드명': 'card_name',
    '카드번호': 'card_no',
    '가격(원)': 'amount',
    '상태': 'res_msg'
  }

  constructor(
    private library: FaIconLibrary
  ) {
    this.library.addIcons(faCreditCard, faReceipt)
  }

  @Input()
  set draft(draft: Draft | undefined) {
    if (draft === undefined) return;
    this._draft = draft;
    this.orderRequest = OrderRequestFromDraft(draft);
    this.tradeRegisterRequest = TradeRegisterRequestFromDraft(draft);
    this.lastPaymentHistory = draft.paymentInfo?.lastHistory;
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
    if (!this._draft) return false;
    if (this._draft.price <= 0) return false;
    if (this._draft.paymentMethod.name !== 'CREDIT_CARD') return false;
    if (this.lastPaymentHistory && this.lastPaymentHistory.paymentStatus === 'PAID') return false;
    return true;
  }

  isPaid() {
    if (!this._draft) return false;
    return this.lastPaymentHistory && this.lastPaymentHistory.paymentStatus === 'PAID';
  }

  print() {
    if (!this.lastPaymentHistory) return;
    const doc = new jsPDF("l", "mm", "a4");
    doc.addFileToVFS('malgun-normal.ttf', malgun);
    doc.addFont('malgun-normal.ttf', 'malgun', 'normal');
    doc.setFont('malgun');

    let x = 20, y = 20, xGap = 40, yGap = 10;

    doc.addImage("assets/header/logo.png", "PNG", x + 1, y, 30, 25);
    y += (yGap * 4);

    doc.setFontSize(18);
    doc.text("영수증", x, y);
    y += 5;

    doc.setFontSize(16);
    y += yGap;
    doc.text("고객명", x, y);
    doc.text(':', x + xGap - 10, y);
    doc.text(`${this._draft?.customerName}`, x + xGap, y);
    for (const [key, value] of Object.entries(this.modalObject)) {
      y += yGap;
      doc.text(key, x, y);
      doc.text(':', x + xGap - 10, y);
      doc.text(`${this.lastPaymentHistory.detail[value]}`, x + xGap, y);
    }
    y += yGap;
    doc.text("거래날짜", x, y);
    doc.text(':', x + xGap - 10, y);
    doc.text(`${this.lastPaymentHistory.createTime}`, x + xGap, y);

    doc.autoPrint({ variant: 'non-conform' });
    doc.save(`donetop-${this.lastPaymentHistory.detail[this.modalObject['거래번호']]}`);
  }

}
