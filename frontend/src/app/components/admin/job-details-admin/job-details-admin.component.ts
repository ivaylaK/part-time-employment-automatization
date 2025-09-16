import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {NgIf, NgForOf, DatePipe} from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbDate, NgbDateStruct, NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';

import { JobService } from '../../../service/job.service';
import { ApplicantService } from '../../../service/applicant.service';
import { JobInvitationService } from '../../../service/job-invitation.service';
import { JobApplicationService } from '../../../service/job-application.service';
import { Job } from '../../../dto/job';
import { Applicant, ApplicantSearch } from '../../../dto/applicant';
import { debounceTime, Subject } from 'rxjs';

@Component({
  selector: 'app-job-details',
  standalone: true,
  imports: [NgIf, NgForOf, FormsModule, NgbDatepickerModule, DatePipe],
  templateUrl: './job-details-admin.component.html',
  styleUrl: './job-details-admin.component.css',
})
export class JobDetailsAdminComponent implements OnInit {
  jobId?: number;
  job?: Job;

  jobApplicants: Applicant[] = [];
  allApplicants: Applicant[] = [];
  searchParams: ApplicantSearch = {};
  searchChange$ = new Subject<void>();

  selectedApplicant?: Applicant;

  minDate?: NgbDateStruct;
  maxDate?: NgbDateStruct;
  selectedDates: NgbDateStruct[] = [];

  constructor(
    private jobService: JobService,
    private applicantService: ApplicantService,
    private jobInvitationService: JobInvitationService,
    private jobApplicationService: JobApplicationService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.jobId = +this.route.snapshot.paramMap.get('id')!;
    if (this.jobId) {
      this.jobService.getById(this.jobId).subscribe({
        next: (job) => {
          this.job = job;
          const start = new Date(`${job.startDate}T00:00:00`);
          const end = new Date(`${job.endDate}T00:00:00`);
          this.minDate = this.toNgb(start);
          this.maxDate = this.toNgb(end);
        },
      });
      this.getJobApplicants(this.jobId);
      this.getAllApplicants();
    }

    this.searchChange$.pipe(debounceTime(200)).subscribe(() => {
      if (this.jobId) this.getJobApplicants(this.jobId);
    });
  }

  private toNgb(date: Date): NgbDateStruct {
    return { year: date.getFullYear(), month: date.getMonth() + 1, day: date.getDate() };
  }

  currentView?: { year: number; month: number };

  onNavigate(next: {year: number; month: number}) {
    this.currentView = next;
  }

  onDateSelection(date: any): void {
    const i = this.selectedDates.findIndex(d =>
      d.year === date.year && d.month === date.month && d.day === date.day
    );
    if (i >= 0) this.selectedDates.splice(i, 1);
    else this.selectedDates.push(date);
  }

  isSelected(date: NgbDateStruct): boolean {
    return this.selectedDates.some(d =>
      d.year === date.year && d.month === date.month && d.day === date.day
    );
  }

  private toDate(d: NgbDateStruct): Date {
    return new Date(d.year, d.month - 1, d.day);
  }

  get chosenDaysIso(): string[] {
    return this.selectedDates
      .slice()
      .sort((a, b) => this.toDate(a).getTime() - this.toDate(b).getTime())
      .map(d => this.toDate(d).toISOString().split('T')[0]);
  }

  markDisabled = (date: NgbDateStruct): boolean => {
    if (!this.minDate || !this.maxDate) return true;
    const t   = new Date(date.year, date.month - 1, date.day).getTime();
    const min = new Date(this.minDate.year, this.minDate.month - 1, this.minDate.day).getTime();
    const max = new Date(this.maxDate.year, this.maxDate.month - 1, this.maxDate.day).getTime();
    return t < min || t > max;
  };

  get monthLabel(): string {
    if (!this.currentView) return '';
    const d = new Date(this.currentView.year, this.currentView.month - 1, 1);
    return d.toLocaleString('en', { month: 'long', year: 'numeric' });
  }


  getJobApplicants(jobId: number): void {
    this.jobService.getApplicantsForJob(jobId).subscribe({
      next: (list) => (this.jobApplicants = list),
    });
  }

  getAllApplicants(): void {
    this.applicantService.getAll().subscribe({
      next: (list) => (this.allApplicants = list),
    });
  }

  availableSlots(): number {
    if (!this.job) return 0;
    return this.job.totalSlots - this.job.applicantsCount;
  }

  addApplicantAsAdmin(): void {
    if (!this.selectedApplicant || !this.jobId) return;
    if (this.chosenDaysIso.length === 0) {
      alert('Please pick at least one date.');
      return;
    }
    this.jobInvitationService
      .addApplicantAsAdmin(this.jobId, this.selectedApplicant.id!, { chosenDays: this.chosenDaysIso })
      .subscribe({
        next: () => {
          alert('Applicant successfully added');
          this.selectedDates = [];
          this.getJobApplicants(this.jobId!);
        },
        error: (err) => alert('Error applying applicant ' + (err?.error ?? '')),
      });
  }

  removeApplicantAsAdmin(applicantId: number): void {
    if (!this.jobId) return;
    this.jobApplicationService.deleteJobApplication(this.jobId, applicantId).subscribe({
      next: () => {
        alert('Applicant removed from the job');
        this.getJobApplicants(this.jobId!);
      },
      error: (err) => alert('Error removing applicant ' + (err?.error ?? '')),
    });
  }
}
