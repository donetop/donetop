import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, QueryList, ViewChildren } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faDownload, faXmark, faCheckCircle } from '@fortawesome/free-solid-svg-icons';
import { TitleComponent } from 'src/app/component/title/title.component';
import { CategoryService } from 'src/app/service/category.service';
import { CryptoService } from 'src/app/service/crypto.service';
import { DraftService } from 'src/app/service/draft.service';
import { EnumService } from 'src/app/service/enum.service';
import { SmsService } from 'src/app/service/sms.service';
import { Category } from 'src/app/store/model/category.model';
import { Enum } from 'src/app/store/model/enum.model';
import { RouteName } from 'src/app/store/model/routeName.model';
import { policy } from './policy';
import { Store } from '@ngrx/store';
import { User, isAdmin } from 'src/app/store/model/user.model';

declare namespace daum {
  type PostcodeData = any;
}
declare const daum: any;

@Component({
  selector: 'app-create',
  standalone: true,
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    FontAwesomeModule,
    TitleComponent
  ]
})
export class CreateComponent implements OnInit {

  categoryArray!: Array<Category>;
  category!: string;
  paymentMethodArray!: Array<Enum>;
  paymentMethod!: string;
  policy: string = policy;
  address: string = '';
  maxSize: number = 3;
  indexArray: Array<number> = new Array(this.maxSize).fill(0).map((v, i) => i);
  @ViewChildren('file') refs!: QueryList<ElementRef>;
  routeName = RouteName.INSTANCE;

  // --- 휴대폰 인증 및 유저 권한 관련 변수 ---
  isAdmin: boolean = false;
  isCodeSent: boolean = false;
  isPhoneVerified: boolean = false;
  verificationCode: string = '';
  phone1Val: string = '';
  phone2Val: string = '';
  phone3Val: string = '';

  // --- 타이머 관련 변수 ---
  readonly DEFAULT_TIMER_SECONDS = 180;
  timerInterval: any = null;
  timerSeconds: number = this.DEFAULT_TIMER_SECONDS;
  isTimerExpired: boolean = false;

  constructor(
    private library: FaIconLibrary, private draftService: DraftService,
    private cryptoService: CryptoService, private enumService: EnumService,
    private categoryService: CategoryService, private smsService: SmsService,
    private router: Router, private store: Store<{ user: User }>
  ) {
    this.library.addIcons(faDownload, faXmark, faCheckCircle);
    this.store.select('user').subscribe(user => this.isAdmin = isAdmin(user));
  }

  async ngOnInit() {
    this.categoryArray = await this.categoryService.categoryArray();
    this.category = this.categoryArray[0].name;
    this.paymentMethodArray = await this.enumService.paymentMethodArray();
    this.paymentMethod = this.paymentMethodArray[0].name;
    document.getElementById('scrollToTopButton')?.click();
  }

  ngOnDestroy() {
    this.stopTimer();
  }

  startTimer() {
    this.stopTimer();
    this.timerSeconds = this.DEFAULT_TIMER_SECONDS;
    this.isTimerExpired = false;

    this.timerInterval = setInterval(() => {
      this.timerSeconds--;
      if (this.timerSeconds <= 0) {
        this.stopTimer();
        this.isTimerExpired = true;
      }
    }, 1000);
  }

  stopTimer() {
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
      this.timerInterval = null;
    }
  }

  get formattedTime(): string {
    const minutes = Math.floor(this.timerSeconds / 60);
    const seconds = this.timerSeconds % 60;
    const minStr = minutes < 10 ? `0${minutes}` : `${minutes}`;
    const secStr = seconds < 10 ? `0${seconds}` : `${seconds}`;
    return `${minStr}:${secStr}`;
  }

  onPhoneChange(event: Event, field: 'phone1Val' | 'phone2Val' | 'phone3Val') {
    const inputElement = event.target as HTMLInputElement;
    const sanitizedValue = inputElement.value.replace(/[^0-9]/g, '');

    inputElement.value = sanitizedValue;
    this[field] = sanitizedValue;

    if (this.isPhoneVerified || this.isCodeSent) {
      this.isCodeSent = false;
      this.isPhoneVerified = false;
      this.verificationCode = '';
      this.stopTimer();
    }
  }

  get fullPhoneNumber(): string {
    return `${this.phone1Val}-${this.phone2Val}-${this.phone3Val}`;
  }

  sendSmsCode() {
    if (!this.phone1Val || !this.phone2Val || !this.phone3Val) {
      alert('휴대폰 번호를 올바르게 입력해주세요.');
      return;
    }

    this.smsService.sendCode(this.fullPhoneNumber).subscribe({
      next: (res) => {
        alert(res.data || '인증번호가 발송되었습니다.');
        this.isCodeSent = true;
        this.verificationCode = '';
        this.startTimer();
      },
      error: ({ error }) => {
        alert(error?.reason || '인증번호 발송에 실패했습니다.');
      }
    });
  }

  verifySmsCode() {
    if (this.isTimerExpired) {
      alert('인증 시간이 만료되었습니다. 인증번호를 재발송 해주세요.');
      return;
    }

    if (!this.verificationCode) {
      alert('인증번호를 입력해주세요.');
      return;
    }

    this.smsService.verifyCode(this.fullPhoneNumber, this.verificationCode).subscribe({
      next: (res) => {
        alert(res.data || '인증이 완료되었습니다.');
        this.isPhoneVerified = true;
        this.stopTimer();
      },
      error: ({ error }) => {
        alert(error?.reason || '인증번호가 일치하지 않습니다.');
      }
    });
  }

  deleteFile(index: number) {
    const fileInput = this.refs.get(index)?.nativeElement;
    fileInput.value = '';
  }

  searchAddress() {
    if (this.address.length > 0) return;
    const component = this;
    new daum.Postcode({
      oncomplete: function (data: daum.PostcodeData) {
        component.address = data.address;
        document.body.click();
        document.getElementById('detailAddress')?.focus();
      }
    }).open();
  }

  onSubmit(form: NgForm) {
    if (!this.isAdmin && !this.isPhoneVerified) {
      alert('휴대폰 인증을 완료해주세요.');
      return;
    }

    if (confirm('정말로 주문하시겠습니까?')) {
      const formData = new FormData();
      formData.append('password', this.cryptoService.encrypt(form.controls['phone3'].value));
      formData.append('categoryName', form.controls['categoryName'].value);
      formData.append('paymentMethod', form.controls['paymentMethod'].value);
      formData.append('estimateContent', form.controls['estimateContent'].value);
      formData.append('companyName', form.controls['companyName'].value);
      formData.append('customerName', form.controls['customerName'].value);
      formData.append('email', form.controls['email'].value);
      formData.append('phoneNumber', this.fullPhoneNumber);
      formData.append('address', form.controls['address'].value);
      if ('detailAddress' in form.controls) formData.append('detailAddress', form.controls['detailAddress'].value);
      else formData.append('detailAddress', '');
      this.refs
        .map(ref => ref.nativeElement.files)
        .filter(files => files.length > 0)
        .map(files => files[0])
        .forEach(file => formData.append('files', file));
      this.draftService.create(formData)
        .subscribe({
          next: (response) => {
            console.log(`draft create success. draft id : ${response.data}`);
            this.router.navigate([this.routeName.DRAFT_LIST], { queryParams: { page: 0 } });
          },
          error: ({error}) => alert(error.reason)
        });
    }
  }
}
