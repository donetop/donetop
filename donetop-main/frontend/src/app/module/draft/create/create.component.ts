import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faDownload } from '@fortawesome/free-solid-svg-icons';
import { TitleComponent } from 'src/app/component/title/title.component';
import { DraftService } from 'src/app/service/draft.service';
import { categories, Category } from 'src/app/store/model/category.model';
import { policy } from './policy';

@Component({
  selector: 'app-create',
  standalone: true,
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    FontAwesomeModule,
    TitleComponent
  ]
})
export class CreateComponent {
  categories: Category[] = categories;
  categoryModel = this.categories[0].name;
  policy: string = policy;

  constructor(private library: FaIconLibrary, private draftService: DraftService) {
    library.addIcons(faDownload);
  }

  onlyNumberKey(event: any) {
    return /^([0-9])$/.test(event.key);
  }

  onSubmit(form: NgForm) {
    this.draftService.create(form);
  }
}
