import { AfterViewInit, Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Draft } from 'src/app/store/model/draft.model';
import { Store } from '@ngrx/store';
import { User, isAdmin } from 'src/app/store/model/user.model';
import { Enum } from 'src/app/store/model/enum.model';
import { EnumService } from 'src/app/service/enum.service';
import { DraftService } from 'src/app/service/draft.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-update',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss']
})
export class UpdateComponent implements AfterViewInit {

  _draft: Draft | undefined;
  @Output() updateEvent = new EventEmitter<UpdateComponent>();
  isAdmin = false;
  draftStatusArray: Array<Enum> = [];
  @ViewChild('draftStatusCancelBtn') draftStatusCancelBtn!: ElementRef;
  @ViewChild('estimateContentCancelBtn') estimateContentCancelBtn!: ElementRef;
  @ViewChild('memoCancelBtn') memoCancelBtn!: ElementRef;
  initialDraftStatusName = '';
  initialEstimateContent = '';
  initialMemo = '';

  constructor(
    private draftService: DraftService, private store: Store<{ user: User }>,
    private enumService: EnumService
  ) {
    this.store.select('user').subscribe(user => this.isAdmin = isAdmin(user));
  }

  async ngAfterViewInit() {
    this.draftStatusArray = await this.enumService.draftStatusArray();
    if (this.isAdmin) {
      this.draftStatusCancelBtn.nativeElement.addEventListener('click', () => this._draft!.draftStatus.name = this.initialDraftStatusName);
      this.estimateContentCancelBtn.nativeElement.addEventListener('click', () => this._draft!.estimateContent = this.initialEstimateContent);
      this.memoCancelBtn.nativeElement.addEventListener('click', () => this._draft!.memo = this.initialMemo);
    }
  }

  @Input()
  set draft(draft: Draft | undefined) {
    if (draft === undefined) return;
    this._draft = draft;
    this.initialDraftStatusName = draft.draftStatus.name;
    this.initialEstimateContent = draft.estimateContent;
    this.initialMemo = draft.memo;
  }

  toggleTarget(target: HTMLSelectElement | HTMLTextAreaElement) {
    target.disabled = !target.disabled;
  }

  toggleButtons(...buttons: HTMLButtonElement[]) {
    buttons.forEach(button => button.classList.toggle('hidden'));
  }

  apply(body: object, target: HTMLSelectElement | HTMLTextAreaElement, ...buttons: HTMLButtonElement[]) {
    let onGoing = 0;
    if (!this.draftStatusCancelBtn.nativeElement.classList.contains('hidden')) onGoing++;
    if (!this.estimateContentCancelBtn.nativeElement.classList.contains('hidden')) onGoing++;
    if (!this.memoCancelBtn.nativeElement.classList.contains('hidden')) onGoing++;
    if (onGoing !== 1) {
      alert('한번에 하나의 값만 변경 가능합니다.');
      return;
    }
    this.draftService.updatePartial(this._draft!!.id, body)
      .subscribe({
        next: (response) => {
          this.updateEvent.emit();
          this.toggleTarget(target);
          this.toggleButtons(...buttons);
        },
        error: ({error}) => alert(error.reason)
      });
  }

}
