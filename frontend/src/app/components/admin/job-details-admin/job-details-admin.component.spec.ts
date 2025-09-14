import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JobDetailsAdminComponent } from './job-details-admin.component';

describe('JobDetailsComponent', () => {
  let component: JobDetailsAdminComponent;
  let fixture: ComponentFixture<JobDetailsAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JobDetailsAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JobDetailsAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
