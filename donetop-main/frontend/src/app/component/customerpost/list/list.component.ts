import { Component, ElementRef, QueryList, ViewChildren } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TitleComponent } from '../../title/title.component';
import { Page } from 'src/app/store/model/page.model';
import { CustomerPost } from 'src/app/store/model/customerpost.model';
import { RouteName } from 'src/app/store/model/routeName.model';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerPostService } from 'src/app/service/customerpost.service';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faMagnifyingGlass, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { SearchCondition } from 'src/app/store/model/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    FontAwesomeModule,
    TitleComponent
  ],
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent {

  page: Page<CustomerPost> | undefined;
  pageNumber: number = 0;
  pageNumberSize: number = 10;
  pageNumbers: Array<number> = [];
  index: Array<number> = [];
  pageCount: number = 10;
  routeName = RouteName.INSTANCE;
  params: any;
  @ViewChildren('checkbox') checkboxes!: QueryList<ElementRef>;
  prevCheckedBoxIndex: number = -1;
  prevCheckedBoxStatus: boolean = true;
  searchConditions: Array<SearchCondition> = [SearchCondition.of('customerName', '고객명'), SearchCondition.of('title', '제목')];
  searchKey: string = this.searchConditions[0].key;
  prevKey: string = this.searchKey;
  searchValue: string = '';

  constructor(
    private route: ActivatedRoute, private customerPostService: CustomerPostService,
    private router: Router, private library: FaIconLibrary
  ) {
    this.route.queryParams.subscribe(params => this.setUp(params));
    this.library.addIcons(faMagnifyingGlass, faPencilAlt);
  }

  setUp(params: any) {
    this.params = Object.assign({}, params);
    this.searchConditions.forEach(condition => {
      const key = condition.key;
      if (key in this.params) {
        this.searchKey = key;
        this.searchValue = this.params[key];
        if (this.searchValue === '') {
          delete this.params[key];
          this.router.navigate([this.routeName.CUSTOMERPOST_LIST], { queryParams: this.params });
        }
      }
    });
    this.prevKey = this.searchKey;
    this.pageNumber = parseInt(this.params['page']);
    this.customerPostService.list(this.params)
      .subscribe({
        next: (response) => {
          this.page = response.data;
          this.pageNumbers = new Array(this.page.totalPages)
            .fill(0)
            .map((v, i) => i)
            .filter(pageNumber => Math.floor(this.pageNumber / this.pageNumberSize) == Math.floor(pageNumber / this.pageNumberSize));
          const startIndex = this.page.totalElements - (this.pageNumber * this.page.size);
          this.index = new Array(this.page.numberOfElements).fill(0).map((v, i) => startIndex - i);
        },
        error: ({error}) => alert(error.reason)
      });
  }

  hasPrev() {
    return this.pageNumbers.length > 0 && this.pageNumbers[0] !== 0;
  }

  hasNext() {
    if (!this.page) return false;
    return this.pageNumbers.length > 0 && this.pageNumbers[this.pageNumbers.length - 1] !== this.page.totalPages - 1;
  }

  goToPage(page: number) {
    this.params['page'] = page;
    this.router.navigate([this.routeName.CUSTOMERPOST_LIST], { queryParams: this.params });
  }

  search() {
    const newParams = Object.assign({}, this.params);
    if (this.prevKey !== this.searchKey) delete newParams[this.prevKey];
    newParams[this.searchKey] = this.searchValue;
    newParams['page'] = 0;
    this.router.navigate([this.routeName.CUSTOMERPOST_LIST], { queryParams: newParams });
  }

  checknthbox(index: number) {
    if (this.prevCheckedBoxIndex != -1 && index === this.prevCheckedBoxIndex) {
      const element = this.checkboxes.get(index)?.nativeElement;
      this.prevCheckedBoxStatus = !this.prevCheckedBoxStatus;
      element.checked = this.prevCheckedBoxStatus;
      return;
    }
    this.checkboxes.forEach((element, i) => element.nativeElement.checked = i === index);
    this.prevCheckedBoxIndex = index;
    this.prevCheckedBoxStatus = true;
  }

  write() {
    this.router.navigate([this.routeName.CUSTOMERPOST_CREATE]);
  }

  detail(id: number) {
    this.router.navigate([this.routeName.CUSTOMERPOST_DETAIL], { queryParams: { id } });
  }

}
