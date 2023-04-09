import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { File } from 'src/app/store/model/file.model';
import { CategoryService } from 'src/app/service/category.service';
import { NgImageSliderModule } from 'ng-image-slider';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    NgImageSliderModule
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  desktop_slide_images: Array<object> = [
    {
      image: 'assets/home/desktop_slide1.jpg',
      thumbImage: 'assets/home/desktop_slide1.jpg',
      alt: 'desktop_slide1',
    },
    {
      image: 'assets/home/desktop_slide2.jpg',
      thumbImage: 'assets/home/desktop_slide2.jpg',
      alt: 'desktop_slide2',
    },
    {
      image: 'assets/home/desktop_slide3.jpg',
      thumbImage: 'assets/home/desktop_slide3.jpg',
      alt: 'desktop_slide3',
    },
    {
      image: 'assets/home/desktop_slide4.jpg',
      thumbImage: 'assets/home/desktop_slide4.jpg',
      alt: 'desktop_slide4',
    },
  ];
  mobile_slide_images: Array<object> = [
    {
      image: 'assets/home/mobile_slide1.jpg',
      thumbImage: 'assets/home/mobile_slide1.jpg',
      alt: 'mobile_slide1',
    },
    {
      image: 'assets/home/mobile_slide2.jpg',
      thumbImage: 'assets/home/mobile_slide2.jpg',
      alt: 'mobile_slide2',
    },
    {
      image: 'assets/home/mobile_slide3.jpg',
      thumbImage: 'assets/home/mobile_slide3.jpg',
      alt: 'mobile_slide3',
    },
    {
      image: 'assets/home/mobile_slide4.jpg',
      thumbImage: 'assets/home/mobile_slide4.jpg',
      alt: 'mobile_slide4',
    },
  ];
  content1_files: Array<File> = [];
  content2_files: Array<File> = [];
  content3_files: Array<File> = [];

  constructor(private categoryService: CategoryService) {}

  async ngOnInit() {
    const categories = await this.categoryService.categoryArray();
    this.content1_files = categories.filter(category => category.name === '현수막')[0].subCategories.flatMap(category => category.folder === undefined ? [] : category.folder.files);
    this.content2_files = categories.filter(category => category.name === '배너')[0].subCategories.flatMap(category => category.folder === undefined ? [] : category.folder.files);
    this.content3_files = categories.filter(category => category.name === '전단지')[0].subCategories.flatMap(category => category.folder === undefined ? [] : category.folder.files);
  }

}
