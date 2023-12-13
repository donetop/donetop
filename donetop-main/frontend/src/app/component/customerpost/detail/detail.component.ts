import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TitleComponent } from '../../title/title.component';
import { CustomerPost } from 'src/app/store/model/customerpost.model';
import { RouteName } from 'src/app/store/model/routeName.model';
import { ActivatedRoute, Router } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { CustomerPostService } from 'src/app/service/customerpost.service';
import { CustomerPostCommentService } from 'src/app/service/customerpost.comment.service';
import { User, isAdmin } from 'src/app/store/model/user.model';
import { Store } from '@ngrx/store';
import { faList, faPencil, faTimes, faTrashCan } from '@fortawesome/free-solid-svg-icons';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-detail',
  standalone: true,
  imports: [
    CommonModule,
    FontAwesomeModule,
    FormsModule,
    TitleComponent
  ],
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent {

  customerPost: CustomerPost | undefined;
  params: any;
  id = 0;
  routeName = RouteName.INSTANCE;
  isAdmin = false;

  constructor(
    private route: ActivatedRoute, private library: FaIconLibrary,
    private customerPostService: CustomerPostService,
    private customerPostCommentService: CustomerPostCommentService,
    private router: Router, private store: Store<{ user: User }>
  ) {
    this.library.addIcons(faTrashCan, faList, faTimes, faPencil);
    this.route.queryParams.subscribe(params => this.setUp(params));
    this.store.select('user').subscribe(user => this.isAdmin = isAdmin(user));
  }

  setUp(params: any) {
    this.params = Object.assign({}, params);
    this.id = parseInt(this.params['id']);
    this.customerPostService.get(this.id)
      .subscribe({
        next: (response) => {
          this.customerPost = response.data;
        },
        error: ({error}) => {
          alert(error.reason);
          this.router.navigate([this.routeName.CUSTOMERPOST_LIST], { queryParams: { page: 0 } });
        }
      });
  }

  delete() {
    if (confirm('정말로 삭제하시겠습니까?')) {
      this.customerPostService.delete(this.id)
      .subscribe({
        next: (response) => this.router.navigate([this.routeName.CUSTOMERPOST_LIST], { queryParams: { page: 0 } }),
        error: ({error}) => alert(error.reason)
      });
    }
  }

  list() {
    this.router.navigate([this.routeName.CUSTOMERPOST_LIST], { queryParams: { page: 0 } });
  }

  customerPostCommentCount() {
    if (!this.customerPost) return 0;
    return this.customerPost.customerPostComments.length;
  }

  addCustomerPostComment(form: NgForm) {
    if (this.customerPost && confirm('댓글을 등록하시겠습니까?')) {
      const formData = new FormData();
      formData.append("customerPostId", `${this.customerPost.id}`);
      formData.append("content", form.controls['commentContent'].value);
      this.customerPostCommentService.create(formData)
        .subscribe({
          next: (response) => {
            console.log(`customer post comment create success. customer post comment id : ${response.data}`);
            this.setUp(this.params);
            form.resetForm();
          },
          error: ({error}) => alert(error.reason)
        });
    }
  }

  deleteCustomerPostComment(id: number) {
    if (confirm('댓글을 삭제하시겠습니까?')) {
      this.customerPostCommentService.delete(id)
        .subscribe({
          next: (response) => {
            this.setUp(this.params);
          },
          error: ({error}) => alert(error.reason)
        });
    }
  }

}
