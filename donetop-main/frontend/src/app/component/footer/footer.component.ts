import { Component, HostListener } from '@angular/core';
import { DesktopComponent } from './desktop/desktop.component';
import { MobileComponent } from './mobile/mobile.component';
import { CommonModule } from '@angular/common';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCircleArrowUp } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FontAwesomeModule,
    DesktopComponent,
    MobileComponent
  ]
})
export class FooterComponent {

  showScrollToTopButton: boolean = false;

  constructor(library: FaIconLibrary) {
    library.addIcons(
      faCircleArrowUp
    );
  }

  @HostListener('window:scroll')
  checkScroll() {
    const scrollPosition = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;
    this.showScrollToTopButton = scrollPosition > 0;
  }

  scrollToTop() {
    window.scroll({
      top: 0,
      left: 0,
      behavior: 'smooth'
    });
  }

}
