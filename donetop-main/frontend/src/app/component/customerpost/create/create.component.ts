import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { TitleComponent } from '../../title/title.component';
import { CustomerPostService } from 'src/app/service/customerpost.service';
import { RouteName } from 'src/app/store/model/routeName.model';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-create',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    TitleComponent
  ],
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss']
})
export class CreateComponent {

  routeName = RouteName.INSTANCE;

  constructor(
    private router: Router, private customerPostService: CustomerPostService,
  ) {}

  cancel() {
    this.router.navigate([this.routeName.CUSTOMERPOST_LIST], { queryParams: { page: 0 } });
  }

  onSubmit(form: NgForm) {
    if (confirm('글을 작성하시겠습니까?')) {
      const formData = new FormData();
      formData.append('customerName', form.controls['customerName'].value);
      formData.append('title', form.controls['title'].value);
      formData.append('content', form.controls['content'].value);
      this.customerPostService.create(formData);
    }
  }

}
