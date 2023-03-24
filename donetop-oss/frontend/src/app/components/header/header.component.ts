import { AfterViewInit, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { RouteName } from 'src/app/store/model/routeName.model';
import { OSSUserService } from 'src/app/service/oss-user.service';
import { Store } from '@ngrx/store';
import { OSSUserLoadAction } from 'src/app/store/action/oss-user.action';
import { OSSUser } from 'src/app/store/model/oss-user.model';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements AfterViewInit {

  routeName = RouteName.INSTANCE;
  ossUser: OSSUser | undefined;

  constructor(
    protected ossUserService: OSSUserService, private store: Store, private ossUserStore: Store<{ ossUser: OSSUser }>,
    private router: Router
  ) {}

  ngAfterViewInit() {
    this.ossUserService.getOSSUserInfo().subscribe({
      next: response => this.store.dispatch(new OSSUserLoadAction(response.data)),
      error: ({error}) => {
        console.log(error.reason);
        this.router.navigateByUrl(this.routeName.LOGIN);
      }
    });
    this.ossUserStore.select('ossUser').subscribe(ossUser => this.ossUser = ossUser);
  }

}
