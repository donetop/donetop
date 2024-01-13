import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Category } from 'src/app/store/model/category.model';
import { Folder } from 'src/app/store/model/folder.model';
import { CategoryService } from 'src/app/service/category.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { RouteName } from 'src/app/store/model/routeName.model';

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent {

  routeName = RouteName.INSTANCE;
  category!: Category;
  folder: Folder | undefined = undefined;

  constructor(
    private categoryService: CategoryService, private route: ActivatedRoute,
    private router: Router
  ) {
    this.route.queryParams.subscribe(params => this.setUp(params));
  }

  async setUp(params: any) {
    this.category = await this.categoryService.get(params['id']);
    this.folder = this.category.folder;
  }

  showDraftCreatePage() {
    this.router.navigate([this.routeName.DRAFT_CREATE]);
  }

}
