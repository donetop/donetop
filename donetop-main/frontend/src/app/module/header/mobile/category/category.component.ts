import { Component, ElementRef, ViewChild, ViewChildren, QueryList, OnInit } from '@angular/core';
import { CategoryService } from 'src/app/service/category.service';
import { Category } from 'src/app/store/model/category.model';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent implements OnInit {

  categories!: Array<Category>;
  showSection: boolean = false;
  @ViewChild('left_section') leftSection!: ElementRef;
  @ViewChild('right_section') rightSection!: ElementRef;
  @ViewChild('category_button') categoryButton!: ElementRef;
  @ViewChild('board_button') boardButton!: ElementRef;
  @ViewChild('category') category!: ElementRef;
  @ViewChild('board') board!: ElementRef;
  @ViewChildren('sub_group_active_buttons') subGroupActiveButtons!: QueryList<ElementRef>;
  @ViewChildren('sub_group_disable_buttons') subGroupDisableButtons!: QueryList<ElementRef>;
  @ViewChildren('sub_groups') subGroups!: QueryList<ElementRef>;

  constructor(private categoryService: CategoryService) {}

  async ngOnInit() {
    this.categories = await this.categoryService.categoryArray();
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

}
