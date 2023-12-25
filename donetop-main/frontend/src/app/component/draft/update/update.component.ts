import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCirclePlus, faDownload, faXmark } from '@fortawesome/free-solid-svg-icons';
import { TitleComponent } from 'src/app/component/title/title.component';
import { CategoryService } from 'src/app/service/category.service';
import { CryptoService } from 'src/app/service/crypto.service';
import { DraftService } from 'src/app/service/draft.service';
import { EnumService } from 'src/app/service/enum.service';
import { Category } from 'src/app/store/model/category.model';
import { Draft } from 'src/app/store/model/draft.model';
import { Enum } from 'src/app/store/model/enum.model';
import { FolderType } from 'src/app/store/model/folder.model';
import { RouteName } from 'src/app/store/model/routeName.model';
import { FilesComponent } from '../../files/files.component';
import { File } from 'src/app/store/model/file.model';

@Component({
  selector: 'app-update',
  standalone: true,
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    FontAwesomeModule,
    TitleComponent,
    FilesComponent
  ]
})
export class UpdateComponent implements OnInit {

  draft: Draft | undefined;
  categoryArray!: Array<Category>;
  paymentMethodArray!: Array<Enum>;
  draftStatusArray!: Array<Enum>;
  id: number = 0;
  password: string = '';
  @ViewChild('filesComponent') filesComponent!: FilesComponent;
  draftWorkFiles: Array<File> = [];
  routeName = RouteName.INSTANCE;

  constructor(
    private route: ActivatedRoute, private draftService: DraftService,
    private enumService: EnumService, private categoryService: CategoryService,
    private library: FaIconLibrary, private cryptoService: CryptoService,
    private router: Router
  ) {
    this.library.addIcons(faDownload, faCirclePlus, faXmark);
    this.route.queryParams.subscribe(params => this.setUp(params));
  }

  async ngOnInit() {
    this.categoryArray = await this.categoryService.categoryArray();
    this.paymentMethodArray = await this.enumService.paymentMethodArray();
    this.draftStatusArray = await this.enumService.draftStatusArray();
    document.getElementById('scrollToTopButton')?.click();
  }

  setUp(params: any) {
    this.id = parseInt(params['id']);
    this.password = params['p'];
    this.draftService.get(this.id, this.password)
      .subscribe({
        next: (response) => {
          this.draft = response.data;
          const files = this.draft?.folders?.find(df => df.folderType === FolderType.DRAFT_WORK)?.files;
          if (files) this.draftWorkFiles = files;
        },
        error: ({error}) => {
          alert(error.reason);
          this.router.navigate([this.routeName.DRAFT_LIST], { queryParams: { page: 0 } });
        }
      });
  }

  onlyNumberKey(event: any) {
    return /^([0-9])$/.test(event.key);
  }

  onSubmit(form: NgForm) {
    const formData = new FormData();
    formData.append('password', this.cryptoService.encrypt(form.controls['phone3'].value));
    formData.append('categoryName', form.controls['categoryName'].value);
    formData.append('paymentMethod', form.controls['paymentMethod'].value);
    formData.append('draftStatus', form.controls['draftStatus'].value);
    formData.append('memo', form.controls['memo'].value);
    formData.append('companyName', form.controls['companyName'].value);
    formData.append('customerName', form.controls['customerName'].value);
    formData.append('email', form.controls['email'].value);
    formData.append('phoneNumber', `${form.controls['phone1'].value}-${form.controls['phone2'].value}-${form.controls['phone3'].value}`);
    formData.append('address', form.controls['address'].value);
    formData.append('detailAddress', form.controls['detailAddress'].value);
    formData.append('price', form.controls['price'].value);
    formData.append('inChargeName', form.controls['inChargeName'].value);
    this.filesComponent.existFiles()
      .forEach(file => formData.append('files', file));
    // formData.forEach((v, k) => console.log(`${k}, ${v}`));
    this.draftService.update(this.id, this.password, formData);
  }

}
