import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DesktopCategoryComponent } from './desktop.category.component';

describe('CategoryComponent', () => {
  let component: DesktopCategoryComponent;
  let fixture: ComponentFixture<DesktopCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DesktopCategoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DesktopCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
