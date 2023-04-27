import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, HostListener, Input, Output } from '@angular/core';

@Component({
  selector: 'app-modal',
  standalone: true,
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  imports: [
    CommonModule
  ]
})
export class ModalComponent {

  @Input() property: Property = Property.default();
  @Output() propertyChange = new EventEmitter<Property>();

  constructor(private eRef: ElementRef) {}

  @HostListener('document:click', ['$event'])
  click(event: PointerEvent) {
    if (this.eRef.nativeElement.contains(event.target)) {
      console.log('Click ModalComponent...');
      if (this.isModal(event)) {
        this.property.toggleShow();
        this.property.reset();
        this.propertyChange.emit(this.property);
      }
    }
  }

  isModal(event: PointerEvent) {
    const classList = document.elementFromPoint(event.clientX, event.clientY)?.classList;
    return classList?.contains('modal');
  }
}

export class Property {
  constructor(public show: boolean, public data: Data, public resetFunc: Function) {}

  static default() {
    return new Property(false, {}, () => {});
  }

  toggleShow() {
    this.show = !this.show;
  }

  reset() {
    this.resetFunc();
  }
}

interface Data {
  [key: string]: any;
}
