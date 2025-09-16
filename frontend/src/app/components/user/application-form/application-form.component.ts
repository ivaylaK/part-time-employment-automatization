import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {DatePipe, NgIf} from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgbDate, NgbDateStruct, NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';

import { JobService } from '../../../service/job.service';
import { JobInvitationService } from '../../../service/job-invitation.service';
import { ApplicantService } from '../../../service/applicant.service';
import { ApplicantDto } from '../../../dto/applicant';
import { Job } from '../../../dto/job';

@Component({
  selector: 'app-application-form',
  standalone: true,
  imports: [NgIf, ReactiveFormsModule, NgbDatepickerModule, DatePipe],
  templateUrl: './application-form.component.html',
  styleUrl: './application-form.component.css',
})
export class ApplicationFormComponent implements OnInit {
  token: string | null = null;
  id: number | null = null;
  form: FormGroup;

  job?: Job;
  minDate?: NgbDateStruct;
  maxDate?: NgbDateStruct;
  selectedDates: NgbDateStruct[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private jobService: JobService,
    private jobInvitationService: JobInvitationService,
    private applicantService: ApplicantService,
  ) {
    this.form = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      number: ['', Validators.required],
      city: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token');
    const jobIdParam = this.route.snapshot.paramMap.get('id');
    if (jobIdParam) this.id = +jobIdParam;

    this.applicantService.getByUser().subscribe({
      next: (a: ApplicantDto) => this.form.patchValue(a),
    });

    if (this.id) {
      this.jobService.getById(this.id).subscribe({
        next: (job) => {
          this.job = job;
          const start = new Date(`${job.startDate}T00:00:00`);
          const end = new Date(`${job.endDate}T00:00:00`);
          this.minDate = this.toNgb(start);
          this.maxDate = this.toNgb(end);
        },
      });
    }
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

  onSubmit(): void {
    if (this.form.invalid) return;
    if (this.selectedDates.length === 0) {
      alert('Please pick at least one date.');
      return;
    }
    const payload = { chosenDays: this.chosenDaysIso };

    if (this.token) {
      this.jobInvitationService.applyToJobWithToken(this.token, payload).subscribe({
        next: () => { alert('Application successful!'); this.router.navigate(['/dashboard']); },
        error: (err) => alert('Application failed: ' + (err?.error ?? '')),
      });
    } else if (this.id) {
      this.jobInvitationService.applyToJobThroughProfile(this.id, payload).subscribe({
        next: () => { alert('Application successful!'); this.router.navigate(['/dashboard']); },
        error: (err) => alert('Application failed: ' + (err?.error ?? '')),
      });
    }
  }
}
