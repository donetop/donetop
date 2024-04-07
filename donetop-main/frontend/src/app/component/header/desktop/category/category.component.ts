import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild, HostListener, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faBars, faXmark } from '@fortawesome/free-solid-svg-icons';
import { CategoryService } from 'src/app/service/category.service';
import { Category } from 'src/app/store/model/category.model';
import { RouteName } from 'src/app/store/model/routeName.model';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FontAwesomeModule
  ]
})
export class CategoryComponent implements OnInit {

  categories: Array<Category> = [];
  showCategory: boolean = false;
  @ViewChild('category_open_button', { static: true }) categoryOpenButton!: ElementRef;
  @ViewChild('category_close_button', { static: true }) categoryCloseButton!: ElementRef;
  @ViewChild('category', { static: true }) category!: ElementRef;
  routeName = RouteName.INSTANCE;

  constructor(
    private eRef: ElementRef, private categoryService: CategoryService,
    private router: Router, private library: FaIconLibrary
  ) {
    this.library.addIcons(faBars, faXmark);
  }

  async ngOnInit() {
    this.categories = (await this.categoryService.categoryArray()).filter(category => category.exposed);
    this.categories.forEach(category => category.subCategories = category.subCategories.filter(category => category.exposed));
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
      else if (this.isATag(event)) this.toggleCategory();
    } else {
      if (this.showCategory) this.toggleCategory();
    }
  }

  private isButton(event: PointerEvent): boolean {
    let tagName = document.elementFromPoint(event.clientX, event.clientY)?.tagName;
    return tagName == 'BUTTON' || tagName == 'FA-ICON' || tagName == 'svg' || tagName == 'path';
  }

  private isATag(event: PointerEvent): boolean {
    const element = document.elementFromPoint(event.clientX, event.clientY);
    if (element?.classList.contains('parent')) return false;
    return element?.tagName == 'A';
  }

  isBottom(index: number) {
    const base = Math.floor(this.categories.length / 5) + Math.floor(this.categories.length % 5) - 1;
    return Math.floor(index / 5) === base;
  }

  showDetail(categoryId: number) {
    this.router.navigate([this.routeName.CATEGORY], { queryParams: { id: categoryId } });
  }
}
