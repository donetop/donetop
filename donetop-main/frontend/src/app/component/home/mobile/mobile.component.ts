import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgImageSliderModule } from 'ng-image-slider';

@Component({
  selector: 'app-mobile',
  standalone: true,
  imports: [
    CommonModule,
    NgImageSliderModule,
  ],
  templateUrl: './mobile.component.html',
  styleUrls: ['./mobile.component.scss']
})
export class MobileComponent {

  imageObject: Array<object> = [
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

}
