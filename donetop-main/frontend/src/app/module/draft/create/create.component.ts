import { CommonModule } from '@angular/common';
import { Component, ElementRef, QueryList, ViewChildren } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faDownload } from '@fortawesome/free-solid-svg-icons';
import { TitleComponent } from 'src/app/component/title/title.component';
import { DraftService } from 'src/app/service/draft.service';
import { categories, Category } from 'src/app/store/model/category.model';
import { PaymentMethod, paymentMethods } from 'src/app/store/model/paymentMethod.model';
import { policy } from './policy';

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
export class CreateComponent {
  categories: Category[] = categories;
  category = this.categories[0].name;
  paymentMethods: PaymentMethod[] = paymentMethods;
  paymentMethod = this.paymentMethods[0].en;
  policy: string = policy;
  @ViewChildren('file') refs!: QueryList<ElementRef>;

  constructor(private library: FaIconLibrary, private draftService: DraftService) {
    this.library.addIcons(faDownload);
  }

  onlyNumberKey(event: any) {
    return /^([0-9])$/.test(event.key);
  }

  onSubmit(form: NgForm) {
    if (confirm('정말로 주문하시겠습니까?')) {
      const formData = new FormData();
      formData.append('password', form.controls['password'].value);
      formData.append('category', form.controls['category'].value);
      formData.append('paymentMethod', form.controls['paymentMethod'].value);
      formData.append('memo', form.controls['memo'].value);
      formData.append('companyName', form.controls['companyName'].value);
      formData.append('customerName', form.controls['customerName'].value);
      formData.append('email', form.controls['email'].value);
      formData.append('phoneNumber', `${form.controls['phone1'].value}-${form.controls['phone2'].value}-${form.controls['phone3'].value}`);
      formData.append('address', form.controls['address'].value);
      this.refs
        .map(ref => ref.nativeElement.files)
        .filter(files => files.length > 0)
        .flatMap(files => files[0])
        .forEach(file => formData.append('files', file));
      this.draftService.create(formData);
    }
  }
}
