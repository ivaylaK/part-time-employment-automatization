import {Component, OnInit} from '@angular/core';
import {debounceTime, Subject} from 'rxjs';
import {Applicant, ApplicantSearch} from '../../../dto/applicant';
import {Router, RouterLink} from '@angular/router';
import {ApplicantService} from '../../../service/applicant.service';
import {FormsModule} from '@angular/forms';
import {DatePipe, NgForOf} from '@angular/common';
import { saveAs } from 'file-saver';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {RankModalComponent} from '../rank-modal/rank-modal.component';
import {BlockedClientsModalComponent} from '../blocked-clients-modal/blocked-clients-modal.component';

declare const bootstrap: any;

@Component({
  selector: 'app-applicants',
  imports: [
    FormsModule,
    NgForOf
  ],
  templateUrl: './applicants.component.html',
  standalone: true,
  styleUrl: './applicants.component.css'
})
export class ApplicantsComponent implements OnInit {

  applicants: Applicant[] = [];
  searchParams: ApplicantSearch = {}
  searchChangeObservable: Subject<void> = new Subject<void>();

  selectedApplicant: Applicant | undefined;

  constructor(private service: ApplicantService,
              private modal: NgbModal) {
  }

  ngOnInit(): void {
    this.getApplicants();
    this.searchChangeObservable.pipe(debounceTime(200))
      .subscribe({next: () => this.getApplicants()});
  }

  getApplicants(): void {
    this.service.getAllByFilter(this.searchParams)
      .subscribe({
        next: applicants => {
          this.applicants = applicants;
        },
        error: err => console.error('Error fetching applicants.', err)
      })
  }

  searchChanged(): void {
    this.searchChangeObservable.next();
  }

  resetFilters(): void {
    this.searchParams = {};
    this.getApplicants();
  }

  editRank(applicant: Applicant): void {
    const rankModal = this.modal.open(RankModalComponent);
    rankModal.componentInstance.rank = applicant.rank ?? null;
    rankModal.componentInstance.firstName = applicant.firstName;
    rankModal.componentInstance.lastName = applicant.lastName;

    this.selectedApplicant = applicant;

    rankModal.closed.subscribe((updatedRank: number | null) => {
      if (updatedRank == null || applicant.id == null) return;
      this.service.updateRank(this.selectedApplicant!.id!, updatedRank)
        .subscribe({
          next: () => {
            this.selectedApplicant!.rank = updatedRank;
          },
          error: err => console.error('Failed to update rank', err)
        });
    });
  }

  editBlockedCompanies(applicant: Applicant): void {
    const blockedCompaniesModal = this.modal.open(BlockedClientsModalComponent);
    blockedCompaniesModal.componentInstance.applicantId = applicant.id;
    blockedCompaniesModal.componentInstance.firstName = applicant.firstName;
    blockedCompaniesModal.componentInstance.lastName = applicant.lastName;
  }

  exportApplicantsToExcel(): void {
    this.service.exportApplicantsToExcel().subscribe({
      next: (response) => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        saveAs(blob, 'applicants.xlsx');
      },
      error: err => console.error('Failed to export applicants', err)
    });
  }
}
