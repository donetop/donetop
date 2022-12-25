import { Component, ElementRef, ViewChild, HostListener } from '@angular/core';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent {
  showCategory: boolean = false;
  @ViewChild('category_open_button', { static: true }) categoryOpenButton!: ElementRef;
  @ViewChild('category_close_button', { static: true }) categoryCloseButton!: ElementRef;
  @ViewChild('category', { static: true }) category!: ElementRef;

  constructor(private eRef: ElementRef) {}

  toggleCategory() {
    this.showCategory = !this.showCategory;
    this.categoryOpenButton.nativeElement.style.visibility = this.showCategory ? 'hidden' : 'visible';
    this.categoryCloseButton.nativeElement.style.visibility = this.showCategory ? 'visible' : 'hidden';
    this.category.nativeElement.style.visibility = this.showCategory ? 'visible' : 'hidden';
  }

  @HostListener('document:click', ['$event'])
  click(event: PointerEvent) {
    if (this.eRef.nativeElement.contains(event.target)) {
      if (!this.showCategory) this.toggleCategory();
      else if (!this.isliTag(event)) this.toggleCategory();
    } else {
      if (this.showCategory) this.toggleCategory();
    }
  }

  private isliTag(event: PointerEvent): boolean {
    return document.elementFromPoint(event.clientX, event.clientY)?.tagName == 'LI';
  }
}
