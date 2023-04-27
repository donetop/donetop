import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PaymentService } from 'src/app/service/payment.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentInfo } from 'src/app/store/model/payment.model';
import { Page } from 'src/app/store/model/page.model';
import { RouteName } from 'src/app/store/model/routeName.model';
import { FormsModule } from '@angular/forms';

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
export class ListComponent {

  routeName = RouteName.INSTANCE;
  params: any;
  page: Page<PaymentInfo> | undefined;
  pageNumber: number = 0;
  pageNumberSize: number = 10;
  pageNumbers: Array<number> = [];
  pageCount: number = 10;
  transactionNumber: string = '';

  constructor(
    private paymentService: PaymentService, private route: ActivatedRoute,
    private router: Router
  ) {
    this.route.queryParams.subscribe(params => this.setUp(params));
  }

  setUp(params: any) {
    this.params = Object.assign({}, params);
    this.pageNumber = parseInt(this.params['page']);
    this.transactionNumber = this.params['lastTransactionNumber'] === undefined ? '' : this.params['lastTransactionNumber'];
    this.paymentService.list(this.params)
      .subscribe({
        next: (response) => {
          this.page = response.data;
          this.pageNumbers = new Array(this.page.totalPages)
            .fill(0)
            .map((v, i) => i)
            .filter(pageNumber => Math.floor(this.pageNumber / this.pageNumberSize) == Math.floor(pageNumber / this.pageNumberSize));
        },
        error: ({error}) => {
          if ('reason' in error) alert(error.reason);
        }
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
    this.router.navigate([this.routeName.PAYMENT_LIST], { queryParams: this.params });
  }

  cancel(paymentInfo: PaymentInfo) {
    if (confirm('정말로 환불하시겠습니까?\n이 작업은 되돌릴 수 없습니다.')) {
      this.paymentService.cancel({ paymentInfoId: paymentInfo.id, pgType: paymentInfo.lastHistory.pgType })
      .subscribe({
        next: (response) => {
          alert(response.data['res_msg']);
          this.router.navigate([this.routeName.HOME]).then(() => {
            this.router.navigate([this.routeName.PAYMENT_LIST], { queryParams: this.params });
          });
        },
        error: ({error}) => alert(error.reason)
      });
    }
  }

  isCanceled(paymentInfo: PaymentInfo) {
    return paymentInfo.lastHistory.paymentStatus === 'CANCELED';
  }

  search() {
    const newParams = Object.assign({}, this.params);
    if (this.transactionNumber.length > 0) newParams['lastTransactionNumber'] = this.transactionNumber;
    else delete newParams['lastTransactionNumber'];
    newParams['page'] = 0;
    this.router.navigate([this.routeName.PAYMENT_LIST], { queryParams: newParams });
  }
}
