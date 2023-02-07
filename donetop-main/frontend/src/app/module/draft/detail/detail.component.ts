import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DraftService } from 'src/app/service/draft.service';
import { Draft } from 'src/app/store/model/draft.model';

@Component({
  selector: 'app-detail',
  standalone: true,
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss'],
  imports: [
    CommonModule
  ]
})
export class DetailComponent {

  draft: Draft | undefined;

  constructor(private route: ActivatedRoute, private draftService: DraftService) {
    this.route.queryParams.subscribe(params => this.setUp(params));
  }

  setUp(params: any) {
    const id = params['id'];
    const password = params['password'];
    this.draftService.get(id, password)
      .subscribe({
        next: (response) => this.draft = response.data,
        error: ({error}) => alert(error.reason)
      });
  }

}
