import {Component, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-application-request-modal',
  imports: [
    FormsModule
  ],
  templateUrl: './application-request-modal.component.html',
  standalone: true,
  styleUrl: './application-request-modal.component.css'
})
export class ApplicationRequestModalComponent {
  @Input() message = '';

  constructor(public modal: NgbActiveModal) {}
}
