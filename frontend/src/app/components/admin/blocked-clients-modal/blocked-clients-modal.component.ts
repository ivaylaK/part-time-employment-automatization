import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {ApplicantService} from '../../../service/applicant.service';
import {JobService} from '../../../service/job.service';
import {FormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-blocked-clients-modal',
  imports: [
    FormsModule,
    NgForOf,
    NgIf
  ],
  templateUrl: './blocked-clients-modal.component.html',
  standalone: true,
  styleUrl: './blocked-clients-modal.component.css'
})
export class BlockedClientsModalComponent implements OnInit {

  @Input() applicantId!: number;
  @Input() firstName = '';
  @Input() lastName = '';

  typedInClient = '';
  selectedFromListClient = '';

  allClients: string[] = [];
  blockedClients: string[] = [];

  constructor(private applicantService: ApplicantService,
              private jobService: JobService,
              public modal: NgbActiveModal) {
  }

  ngOnInit(): void {
    this.jobService.getAllClients()
      .subscribe(
        clients => this.allClients = clients ?? []
      );

    this.applicantService.getBlockedClients(this.applicantId)
      .subscribe(
        clients => this.blockedClients = clients ?? []
      );
  }

  addTypedIn(): void {
    const client = this.typedInClient.trim();
    if (!client && this.blockedClients.some(b => b.toLowerCase() === client.toLowerCase())) return;

    this.add(client);
    this.typedInClient = '';
  }

  addSelectedFromList(): void {
    if (!this.selectedFromListClient) return;

    this.add(this.selectedFromListClient);
    this.selectedFromListClient = '';
  }

  add(client: string): void {
    this.applicantService.addBlockedClient(this.applicantId, client)
      .subscribe({
        next: () => {
          this.blockedClients = [...this.blockedClients, client];
        }
      });
  }

  remove(client: string): void {
    this.applicantService.removeBlockedClient(this.applicantId, client)
      .subscribe({
        next: () => {
          this.blockedClients = this.blockedClients.filter(b => b.toLowerCase() !== client.toLowerCase());
        }
      });
  }
}
