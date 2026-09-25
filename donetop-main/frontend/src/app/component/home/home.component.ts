import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, OnInit } from '@angular/core';
import { File } from 'src/app/store/model/file.model';
import { CategoryService } from 'src/app/service/category.service';
import { RouteName } from 'src/app/store/model/routeName.model';
import { Router, RouterModule } from '@angular/router';
import { ExtensionHidePipe } from 'src/app/pipe/extensionhide.pipe';

declare const $: Function;

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ExtensionHidePipe
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, AfterViewInit {

  main_desktop_slider_image_urls = [
    "/assets/home/desktop_slide1.jpg",
    "/assets/home/desktop_slide2.jpg",
    "/assets/home/desktop_slide3.jpg",
    "/assets/home/desktop_slide4.jpg",
  ]
  main_mobile_slider_image_urls = [
    "/assets/home/mobile_slide1.jpg",
    "/assets/home/mobile_slide2.jpg",
    "/assets/home/mobile_slide3.jpg",
    "/assets/home/mobile_slide4.jpg",
  ]
  hyeonsumak_files: Array<File> = [];
  hyeonsumak_fileGroups: Array<FileGroup> = [];
  hyeonsumak_fileGroup_size = 4;
  hyeonsumak_image_load_count = 0;
  banner_files: Array<File> = [];
  jeondanji_files: Array<File> = [];
  routeName = RouteName.INSTANCE;

  constructor(private categoryService: CategoryService, private router: Router) {}

  async ngOnInit() {
    const categories = await this.categoryService.categoryArray();
    this.hyeonsumak_files = categories.filter(category => category.name === '현수막')[0].subCategories.flatMap(category => category.folder === undefined ? [] : category.folder.files);
    this.hyeonsumak_fileGroups = this.buildFileGroupFrom(this.hyeonsumak_files);
    this.banner_files = categories.filter(category => category.name === '배너')[0].subCategories.flatMap(category => category.folder === undefined ? [] : category.folder.files);
    this.jeondanji_files = categories.filter(category => category.name === '전단지')[0].subCategories.flatMap(category => category.folder === undefined ? [] : category.folder.files);
  }

  ngAfterViewInit() {
    $(".main_desktop_slider").bxSlider({
      auto: 'false',
      speed : 500, // 슬라이드 속도
      pause : 5000, // 멈춰있는 시간
      mode :'fade', // 이미지 전환효과 (fade, vertical, horizontal)
      infiniteLoop : true, // 무한루프 (true / false)
      autoHover : false, // 마우스 오버시 멈춤 (true / false)
      pager : true,
      controls : false,
    });
    $(".main_mobile_slider").bxSlider({
      auto: 'false',
      speed : 500, // 슬라이드 속도
      pause : 5000, // 멈춰있는 시간
      mode :'fade', // 이미지 전환효과 (fade, vertical, horizontal)
      infiniteLoop : true, // 무한루프 (true / false)
      autoHover : false, // 마우스 오버시 멈춤 (true / false)
      pager : true,
      controls : false,
    });
  }

  loadComplete() {
    this.hyeonsumak_image_load_count++;
    if (this.hyeonsumak_image_load_count === this.hyeonsumak_files.length) {
      $(".banner_slider").bxSlider({
        auto: 'false',
        speed: 500, // 슬라이드 속도
        pause: 5000, // 멈춰있는 시간
        mode: 'vertical',
        infiniteLoop: true, // 무한루프 (true / false)
        autoHover: false, // 마우스 오버시 멈춤 (true / false)
        pager: false,
        controls: false,
      });
    }
  }

  buildFileGroupFrom(files: Array<File>): Array<FileGroup> {
    const fileGroups: Array<FileGroup> = [];
    for (let i = 0; i < files.length; i += this.hyeonsumak_fileGroup_size) {
      fileGroups.push(new FileGroup(files.slice(i, i + this.hyeonsumak_fileGroup_size)));
    }
    return fileGroups;
  }

  showDraftCreatePage() {
    this.router.navigate([this.routeName.DRAFT_CREATE]);
  }

}

class FileGroup {
  files: Array<File> = [];
  constructor(files: Array<File>) {
    this.files = files;
  }
}
