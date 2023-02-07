import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { TitleComponent } from 'src/app/component/title/title.component';
import { RandomEmployeePipe } from 'src/app/pipe/random-employee.pipe';
import { DraftService } from 'src/app/service/draft.service';
import { Draft } from 'src/app/store/model/draft.model';
import { Employee, employees } from 'src/app/store/model/employee.model';
import { Page } from 'src/app/store/model/page.model';
import { ModalComponent, Property } from '../modal/modal.component';

@Component({
  selector: 'app-list',
  standalone: true,
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
  imports: [
    CommonModule,
    RouterModule,
    RandomEmployeePipe,
    TitleComponent,
    ModalComponent
  ]
})
export class ListComponent {

  page: Page<Draft> | undefined;
  pageNumber: number = 0;
  pageNumberSize: number = 10;
  pageNumbers: Array<number> = [];
  employees: Employee[] = employees;
  modalProperty: Property<number> = Property.default();

  constructor(private route: ActivatedRoute, private draftService: DraftService) {
    this.route.queryParams.subscribe(params => this.setUp(params));
  }

  setUp(params: any) {
    this.pageNumber = parseInt(params['page']);
    this.draftService.list(this.pageNumber)
      .subscribe({
        next: (response) => {
          this.page = response.data;
          this.pageNumbers = new Array(this.page.totalPages)
            .fill(0)
            .map((v, i) => i)
            .filter(pageNumber => Math.floor(this.pageNumber / this.pageNumberSize) == Math.floor(pageNumber / this.pageNumberSize));
        },
        error: ({error}) => alert(error.reason)
      });
  }

  openModal(id: number) {
    this.modalProperty.toggleShow();
    this.modalProperty.id = id;
  }

}
