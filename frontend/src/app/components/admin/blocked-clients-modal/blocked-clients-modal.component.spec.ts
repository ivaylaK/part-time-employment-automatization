import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BlockedClientsModalComponent } from './blocked-clients-modal.component';

describe('BlockedClientsModalComponent', () => {
  let component: BlockedClientsModalComponent;
  let fixture: ComponentFixture<BlockedClientsModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BlockedClientsModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BlockedClientsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
