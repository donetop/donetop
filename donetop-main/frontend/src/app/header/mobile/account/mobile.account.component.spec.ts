import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MobileAccountComponent } from './mobile.account.component';

describe('AccountComponent', () => {
  let component: MobileAccountComponent;
  let fixture: ComponentFixture<MobileAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MobileAccountComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MobileAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
