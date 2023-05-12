import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouteName } from 'src/app/store/model/routeName.model';
import { RouterModule } from '@angular/router';

declare const $: any;

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  routeName = RouteName.INSTANCE;

  ngOnInit() {
    // https://github.com/ColorlibHQ/AdminLTE/issues/2607
    $(function() { const trees: any = $('[data-widget = "treeview"]'); trees.Treeview('init'); });
  }

}
