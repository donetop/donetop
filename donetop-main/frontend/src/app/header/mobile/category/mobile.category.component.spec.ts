import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MobileCategoryComponent } from './mobile.category.component';

describe('CategoryComponent', () => {
  let component: MobileCategoryComponent;
  let fixture: ComponentFixture<MobileCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MobileCategoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MobileCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
