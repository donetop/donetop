import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, HostListener, Input, Output } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { CryptoService } from 'src/app/service/crypto.service';
import { DraftService } from 'src/app/service/draft.service';
import { RouteName } from 'src/app/store/model/routeName.model';

@Component({
  selector: 'app-modal',
  standalone: true,
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  imports: [
    CommonModule,
    FormsModule
  ]
})
export class ModalComponent {

  @Input() property: Property<number> = Property.default();
  @Output() propertyChange = new EventEmitter<Property<number>>();

  constructor(
    private eRef: ElementRef, private router: Router,
    private draftService: DraftService, private routeName: RouteName,
    private cryptoService: CryptoService
  ) {}

  @HostListener('document:click', ['$event'])
  click(event: PointerEvent) {
    if (this.eRef.nativeElement.contains(event.target)) {
      console.log('Click ModalComponent...');
      if (this.isModal(event)) {
        this.property.toggleShow();
        this.propertyChange.emit(this.property);
      }
    }
  }

  isModal(event: PointerEvent) {
    const classList = document.elementFromPoint(event.clientX, event.clientY)?.classList;
    return classList?.contains('modal');
  }

  onSubmit(form: NgForm) {
    const id = this.property.id;
    const password = this.cryptoService.encrypt(form.controls['password'].value);
    this.draftService.get(id, password)
      .subscribe({
        next: (response) => {
          this.router.navigate([this.routeName.DRAFT_DETAIL], { queryParams: { id, p: password } });
        },
        error: ({error}) => alert(error.reason)
      });
  }
}

export class Property<Id> {
  constructor(public show: boolean, public id: Id) {}

  static default() {
    return new Property<number>(false, -1);
  }

  toggleShow() {
    this.show = !this.show;
  }
}
