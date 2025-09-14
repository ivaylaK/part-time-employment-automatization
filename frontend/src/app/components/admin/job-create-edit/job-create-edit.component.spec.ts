import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JobCreateEditComponent } from './job-create-edit.component';

describe('JobCreateComponent', () => {
  let component: JobCreateEditComponent;
  let fixture: ComponentFixture<JobCreateEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JobCreateEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JobCreateEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
