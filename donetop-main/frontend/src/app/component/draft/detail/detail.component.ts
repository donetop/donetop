import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCopy } from '@fortawesome/free-regular-svg-icons';
import { faPenToSquare, faPrint, faTrashCan } from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { TitleComponent } from 'src/app/component/title/title.component';
import { DraftService } from 'src/app/service/draft.service';
import { Draft } from 'src/app/store/model/draft.model';
import { RouteName } from 'src/app/store/model/routeName.model';
import { isAdmin, User } from 'src/app/store/model/user.model';
import { DraftFolder, FolderType } from 'src/app/store/model/folder.model';
import { FileSizePipe } from 'src/app/pipe/filesize.pipe';
import { FilesComponent } from '../../files/files.component';
import { FormsModule } from '@angular/forms';
import { ZeroPricePipe } from 'src/app/pipe/zeroprice.pipe';
import { Enum } from 'src/app/store/model/enum.model';
import { EnumService } from 'src/app/service/enum.service';
import { CommentComponent } from './comment/comment.component';
import { PaymentComponent } from './payment/payment.component';

@Component({
  selector: 'app-detail',
  standalone: true,
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss'],
  imports: [
    CommonModule,
    FontAwesomeModule,
    TitleComponent,
    PaymentComponent,
    FileSizePipe,
    ZeroPricePipe,
    FilesComponent,
    FormsModule,
    CommentComponent
  ]
})
export class DetailComponent implements AfterViewInit {

  draft: Draft | undefined;
  params: any;
  orderFolder: DraftFolder | undefined;
  workFolder: DraftFolder | undefined;
  isAdmin: boolean = false;
  id: number = 0;
  password: string = '';
  private routeName = RouteName.INSTANCE;
  @ViewChild('commentComponent') commentComponent!: CommentComponent;
  draftStatusArray!: Array<Enum>;
  @ViewChild('draftStatusSelect') draftStatusSelect!: ElementRef;
  draftStatusName = '';

  constructor(
    private route: ActivatedRoute, private draftService: DraftService,
    private store: Store<{ user: User }>, private library: FaIconLibrary,
    private router: Router, private enumService: EnumService
  ) {
    this.library.addIcons(
      faTrashCan, faPenToSquare, faCopy, faPrint
    );
    this.route.queryParams.subscribe(params => this.setUp(params));
    this.store.select('user').subscribe(user => this.isAdmin = isAdmin(user));
  }

  async ngAfterViewInit() {
    document.getElementById('scrollToTopButton')?.click();
    this.commentComponent.commentEvent.subscribe(() => this.setUp(this.params));
    this.draftStatusArray = await this.enumService.draftStatusArray();
  }

  setUp(params: any) {
    this.params = Object.assign({}, params);
    this.id = parseInt(this.params['id']);
    this.password = this.params['p'];
    this.draftService.get(this.id, this.password)
      .subscribe({
        next: (response) => {
          this.draft = response.data;
          this.draftStatusName = this.draft.draftStatus.name;
          this.orderFolder = this.draft.folders.find(df => df.folderType === FolderType.DRAFT_ORDER);
          this.workFolder = this.draft.folders.find(df => df.folderType === FolderType.DRAFT_WORK);
        },
        error: ({error}) => {
          alert(error.reason);
          this.router.navigate([this.routeName.DRAFT_LIST], { queryParams: { page: 0 } });
        }
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

  openAtBlank(url: string) {
    window.open(url, '_blank');
  }

  requestPrint() {
    if (confirm(`인쇄 요청은 되돌릴 수 없습니다.\n인쇄 요청 하시겠습니까?`)) {
      this.draftService.updateStatus(this.draft!!.id, "PRINT_REQUEST")
      .subscribe({
        next: (response) => {
          alert("인쇄 요청 완료")
          this.setUp(this.params);
        },
        error: ({error}) => alert(error.reason)
      });
    }
  }

  clickStatusBtn(statusBtn: HTMLButtonElement, cancelBtn: HTMLButtonElement) {
    const content = statusBtn.textContent;
    if (content === '수정') {
      statusBtn.textContent = '적용';
      cancelBtn.classList.toggle('hidden');
      this.draftStatusSelect.nativeElement.disabled = false;
    } else if (content === '적용') {
      this.draftService.updateStatus(this.draft!!.id, this.draftStatusSelect.nativeElement.value)
        .subscribe({
          next: (response) => {
            this.setUp(this.params);
            statusBtn.textContent = '수정';
            cancelBtn.classList.toggle('hidden');
            this.draftStatusSelect.nativeElement.disabled = true;
          },
          error: ({error}) => alert(error.reason)
        });
    }
  }

  clickCancelBtn(statusBtn: HTMLButtonElement, cancelBtn: HTMLButtonElement) {
    statusBtn.textContent = '수정';
    cancelBtn.classList.toggle('hidden');
    this.draftStatusSelect.nativeElement.disabled = true;
    this.draftStatusSelect.nativeElement.value = this.draft?.draftStatus.name;
  }

}
