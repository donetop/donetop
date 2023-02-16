import { Component, ElementRef, ViewChild, HostListener, OnInit } from '@angular/core';
import { EnumService } from 'src/app/service/enum.service';
import { Category } from 'src/app/store/model/category.model';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent implements OnInit {

  categories!: Category[];
  showCategory: boolean = false;
  @ViewChild('category_open_button', { static: true }) categoryOpenButton!: ElementRef;
  @ViewChild('category_close_button', { static: true }) categoryCloseButton!: ElementRef;
  @ViewChild('category', { static: true }) category!: ElementRef;

  constructor(private eRef: ElementRef, private enumService: EnumService) {}

  async ngOnInit() {
    this.categories = await this.enumService.categoryArray();
  }

  toggleCategory() {
    this.showCategory = !this.showCategory;
    this.categoryOpenButton.nativeElement.style.visibility = this.showCategory ? 'hidden' : 'visible';
    this.categoryCloseButton.nativeElement.style.visibility = this.showCategory ? 'visible' : 'hidden';
    this.category.nativeElement.style.visibility = this.showCategory ? 'visible' : 'hidden';
  }

  @HostListener('document:click', ['$event'])
  click(event: PointerEvent) {
    if (this.eRef.nativeElement.contains(event.target)) {
      console.log('Click DesktopCategoryComponent...');
      if (!this.showCategory) this.toggleCategory();
      else if (this.isButton(event)) this.toggleCategory();
    } else {
      if (this.showCategory) this.toggleCategory();
    }
  }

  private isButton(event: PointerEvent): boolean {
    let tagName = document.elementFromPoint(event.clientX, event.clientY)?.tagName;
    return tagName == 'BUTTON' || tagName == 'FA-ICON' || tagName == 'svg' || tagName == 'path';
  }
}
