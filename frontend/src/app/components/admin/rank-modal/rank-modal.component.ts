import {Component, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-rank-modal',
  imports: [
    FormsModule
  ],
  templateUrl: './rank-modal.component.html',
  standalone: true,
  styleUrl: './rank-modal.component.css'
})
export class RankModalComponent {
  @Input() rank: number | null = null;
  @Input() firstName = '';
  @Input() lastName = '';

  constructor(public modal: NgbActiveModal) {
  }

  save(): void {
    this.modal.close(this.rank);
  }
}
