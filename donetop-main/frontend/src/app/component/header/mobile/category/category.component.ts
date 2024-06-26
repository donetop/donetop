import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild, ViewChildren, QueryList, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faBars, faAngleRight, faCaretRight, faAngleDown } from '@fortawesome/free-solid-svg-icons';
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

  categories!: Array<Category>;
  showSection: boolean = false;
  routeName = RouteName.INSTANCE;
  @ViewChild('left_section') leftSection!: ElementRef;
  @ViewChild('right_section') rightSection!: ElementRef;
  @ViewChild('category_button') categoryButton!: ElementRef;
  @ViewChild('board_button') boardButton!: ElementRef;
  @ViewChild('category') category!: ElementRef;
  @ViewChild('board') board!: ElementRef;
  @ViewChildren('sub_group_active_buttons') subGroupActiveButtons!: QueryList<ElementRef>;
  @ViewChildren('sub_group_disable_buttons') subGroupDisableButtons!: QueryList<ElementRef>;
  @ViewChildren('sub_groups') subGroups!: QueryList<ElementRef>;

  constructor(
    private categoryService: CategoryService, private router: Router,
    private library: FaIconLibrary
  ) {
    this.library.addIcons(
      faBars, faAngleRight,
      faAngleDown, faCaretRight
    );
  }

  async ngOnInit() {
    this.categories = (await this.categoryService.categoryArray()).filter(category => category.exposed);
    this.categories.forEach(category => category.subCategories = category.subCategories.filter(category => category.exposed));
  }

  toggleSection() {
    this.showSection = !this.showSection;
    if (this.showSection) {
      this.leftSection.nativeElement.classList.add('active');
      this.rightSection.nativeElement.classList.add('active');
    } else {
      this.leftSection.nativeElement.classList.remove('active');
      this.rightSection.nativeElement.classList.remove('active');
    }
  }

  toggleMenu(showCategory: boolean) {
    if (showCategory) {
      this.categoryButton.nativeElement.classList.add('active');
      this.category.nativeElement.classList.add('active');
      this.boardButton.nativeElement.classList.remove('active');
      this.board.nativeElement.classList.remove('active');
    } else {
      this.boardButton.nativeElement.classList.add('active');
      this.board.nativeElement.classList.add('active');
      this.categoryButton.nativeElement.classList.remove('active');
      this.category.nativeElement.classList.remove('active');
    }
  }

  toggleSubGroup(index: number) {
    this.subGroupActiveButtons.filter((element, i) => i === index)[0].nativeElement.classList.toggle('active');
    this.subGroupDisableButtons.filter((element, i) => i === index)[0].nativeElement.classList.toggle('active');
    this.subGroups.filter((element, i) => i === index)[0].nativeElement.classList.toggle('active');
  }

  movePage(url: string, params: object = {}) {
    this.router.navigate([url], { queryParams: params });
    this.toggleSection();
  }

}
