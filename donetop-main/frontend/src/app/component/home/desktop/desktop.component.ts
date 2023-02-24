import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgImageSliderModule } from 'ng-image-slider';

@Component({
  selector: 'app-desktop',
  standalone: true,
  imports: [
    CommonModule,
    NgImageSliderModule
  ],
  templateUrl: './desktop.component.html',
  styleUrls: ['./desktop.component.scss']
})
export class DesktopComponent {

  imageObject: Array<object> = [
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

}
