import {Component, Input, OnInit} from '@angular/core';
import {Request} from '../../../dto/request';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {RequestService} from '../../../service/request.service';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-messages-modal-admin',
  imports: [
    NgIf,
    NgForOf
  ],
  templateUrl: './messages-modal-admin.component.html',
  standalone: true,
  styleUrl: './messages-modal-admin.component.css'
})
export class MessagesModalAdminComponent implements OnInit {

  @Input() requests: Request[] = [];
  selected: number | null = null;

  constructor(public modal: NgbActiveModal,
              private applicationRequestService: RequestService) {}

  ngOnInit(): void {
    this.applicationRequestService.getAll().subscribe({
      next: (requests) => {
        this.requests = requests;
      }
    });
  }

  approve(id: number): void {
    this.selected = id;
    this.applicationRequestService.approveAndDelete(id).subscribe({
      next: () => {
        this.requests = this.requests.filter(r => r.id !== id);
        this.selected = null;
      },
      error: () => { this.selected = null; }
    });
  }

  remove(id: number): void {
    this.selected = id;
    this.applicationRequestService.delete(id).subscribe({
      next: () => {
        this.requests = this.requests.filter(r => r.id !== id);
        this.selected = null;
      },
      error: () => { this.selected = null; }
    });
  }
}
