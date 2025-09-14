import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MessagesModalAdminComponent } from './messages-modal-admin.component';

describe('MessagesModalAdminComponent', () => {
  let component: MessagesModalAdminComponent;
  let fixture: ComponentFixture<MessagesModalAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MessagesModalAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MessagesModalAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
