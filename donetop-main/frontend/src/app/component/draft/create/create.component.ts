import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, QueryList, ViewChildren } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faDownload, faXmark } from '@fortawesome/free-solid-svg-icons';
import { TitleComponent } from 'src/app/component/title/title.component';
import { CategoryService } from 'src/app/service/category.service';
import { CryptoService } from 'src/app/service/crypto.service';
import { DraftService } from 'src/app/service/draft.service';
import { EnumService } from 'src/app/service/enum.service';
import { Category } from 'src/app/store/model/category.model';
import { Enum } from 'src/app/store/model/enum.model';
import { RouteName } from 'src/app/store/model/routeName.model';
import { policy } from './policy';

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

  constructor(
    private library: FaIconLibrary, private draftService: DraftService,
    private cryptoService: CryptoService, private enumService: EnumService,
    private categoryService: CategoryService, private router: Router
  ) {
    this.library.addIcons(faDownload, faXmark);
  }

  async ngOnInit() {
    this.categoryArray = await this.categoryService.categoryArray();
    this.category = this.categoryArray[0].name;
    this.paymentMethodArray = await this.enumService.paymentMethodArray();
    this.paymentMethod = this.paymentMethodArray[0].name;
    document.getElementById('scrollToTopButton')?.click();
  }

  onlyNumberKey(event: any) {
    return /^([0-9])$/.test(event.key);
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
    if (confirm('정말로 주문하시겠습니까?')) {
      const formData = new FormData();
      formData.append('password', this.cryptoService.encrypt(form.controls['phone3'].value));
      formData.append('categoryName', form.controls['categoryName'].value);
      formData.append('paymentMethod', form.controls['paymentMethod'].value);
      formData.append('estimateContent', form.controls['estimateContent'].value);
      formData.append('companyName', form.controls['companyName'].value);
      formData.append('customerName', form.controls['customerName'].value);
      formData.append('email', form.controls['email'].value);
      formData.append('phoneNumber', `${form.controls['phone1'].value}-${form.controls['phone2'].value}-${form.controls['phone3'].value}`);
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
