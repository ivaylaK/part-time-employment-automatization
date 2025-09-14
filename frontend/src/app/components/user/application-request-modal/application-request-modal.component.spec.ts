import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationRequestModalComponent } from './application-request-modal.component';

describe('ApplicationRequestModalComponent', () => {
  let component: ApplicationRequestModalComponent;
  let fixture: ComponentFixture<ApplicationRequestModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationRequestModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApplicationRequestModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
