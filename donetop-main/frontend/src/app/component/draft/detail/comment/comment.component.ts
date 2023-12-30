import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Draft } from 'src/app/store/model/draft.model';
import { FilesComponent } from 'src/app/component/files/files.component';
import { FormsModule, NgForm } from '@angular/forms';
import { FileSizePipe } from 'src/app/pipe/filesize.pipe';
import { User, isAdmin } from 'src/app/store/model/user.model';
import { Store } from '@ngrx/store';
import { DraftCommentService } from 'src/app/service/draft.comment.service';
import { faDownload, faPencil, faTimes } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-comment',
  standalone: true,
  imports: [
    CommonModule,
    FontAwesomeModule,
    FormsModule,
    FilesComponent,
    FileSizePipe
  ],
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss']
})
export class CommentComponent {

  @Input() draft: Draft | undefined;
  isAdmin = false;
  @ViewChild('filesComponent') filesComponent!: FilesComponent;
  @Output() commentEvent = new EventEmitter<CommentComponent>();

  constructor(
    private library: FaIconLibrary, private store: Store<{ user: User }>,
    private draftCommentService: DraftCommentService,
  ) {
    this.library.addIcons(faDownload, faPencil, faTimes);
    this.store.select('user').subscribe(user => this.isAdmin = isAdmin(user));
  }

  commentCount() {
    if (!this.draft) return 0;
    return this.draft.draftComments.length;
  }

  addComment(form: NgForm) {
    if (this.draft && confirm('댓글을 등록하시겠습니까?')) {
      const formData = new FormData();
      formData.append("draftId", `${this.draft.id}`);
      formData.append("content", form.controls['commentContent'].value);
      this.filesComponent.existFiles()
        .forEach(file => formData.append('files', file));
      // formData.forEach((v, k) => console.log(`${k}, ${v}`));
      this.draftCommentService.create(formData)
        .subscribe({
          next: (response) => {
            console.log(`draft comment create success. draft comment id : ${response.data}`);
            this.commentEvent.emit();
            form.resetForm();
            this.filesComponent.reset();
          },
          error: ({error}) => alert(error.reason)
        });
    }
  }

  deleteComment(id: number) {
    if (confirm('댓글을 삭제하시겠습니까?')) {
      this.draftCommentService.delete(id)
        .subscribe({
          next: (response) => {
            this.commentEvent.emit();
          },
          error: ({error}) => alert(error.reason)
        });
    }
  }

}
