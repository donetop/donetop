import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faDownload } from '@fortawesome/free-solid-svg-icons';
import { TitleComponent } from 'src/app/component/title/title.component';
import { categories, Category } from 'src/app/store/model/category';

@Component({
  selector: 'app-create',
  standalone: true,
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
    FontAwesomeModule,
    TitleComponent
  ]
})
export class CreateComponent {
  categories: Category[] = categories;
  categoryModel = this.categories[0].name;

  constructor(library: FaIconLibrary) {
    library.addIcons(faDownload);
  }

  onlyNumberKey(event: any) {
    return /^([0-9])$/.test(event.key);
  }
}
