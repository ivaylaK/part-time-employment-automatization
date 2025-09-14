import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MessagesModalUserComponent } from './messages-modal-user.component';

describe('MessagesModalUserComponent', () => {
  let component: MessagesModalUserComponent;
  let fixture: ComponentFixture<MessagesModalUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MessagesModalUserComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MessagesModalUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
