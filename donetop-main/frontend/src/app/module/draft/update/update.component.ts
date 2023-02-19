import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { TitleComponent } from 'src/app/component/title/title.component';
import { DraftService } from 'src/app/service/draft.service';
import { EnumService } from 'src/app/service/enum.service';
import { Category, Enum } from 'src/app/store/model/category.model';
import { Draft } from 'src/app/store/model/draft.model';
import { RouteName } from 'src/app/store/model/routeName.model';

@Component({
  selector: 'app-update',
  standalone: true,
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    TitleComponent
  ]
})
export class UpdateComponent implements OnInit {

  draft: Draft | undefined;
  categoryArray!: Category[];
  paymentMethodArray!: Enum[];
  id: number = 0;
  password: string = '';

  constructor(
    private route: ActivatedRoute, private draftService: DraftService,
    private enumService: EnumService, protected routeName: RouteName,
  ) {
    this.route.queryParams.subscribe(params => this.setUp(params));
  }

  async ngOnInit() {
    this.categoryArray = await this.enumService.categoryArray();
    this.paymentMethodArray = await this.enumService.paymentMethodArray();
  }

  setUp(params: any) {
    this.id = parseInt(params['id']);
    this.password = params['p'];
    this.draftService.get(this.id, this.password)
      .subscribe({
        next: (response) => this.draft = response.data,
        error: ({error}) => alert(error.reason)
      });
  }

  onSubmit(form: NgForm) {
    console.log(this.draft);
  }

}
