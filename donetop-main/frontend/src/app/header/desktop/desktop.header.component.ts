import { Component, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'app-desktop-header',
  templateUrl: './desktop.header.component.html',
  styleUrls: ['./desktop.header.component.scss']
})
export class DesktopHeaderComponent {
  showCategory: boolean = false;
  @ViewChild('category_open_button', { static: true }) categoryOpenButton!: ElementRef;
  @ViewChild('category_close_button', { static: true }) categoryCloseButton!: ElementRef;
  @ViewChild('category', { static: true }) category!: ElementRef;

  toggleCategory() {
    this.showCategory = !this.showCategory;
    this.categoryOpenButton.nativeElement.style.visibility = this.showCategory ? "hidden" : "visible";
    this.categoryCloseButton.nativeElement.style.visibility = this.showCategory ? "visible" : "hidden";
    this.category.nativeElement.style.visibility = this.showCategory ? "visible" : "hidden";
  }

  test() {
    console.log("test");
  }
}
