import {Component, OnInit} from '@angular/core';
import {Job} from '../../../dto/job';
import {JobService} from '../../../service/job.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ApplicantService} from '../../../service/applicant.service';
import {DatePipe, NgIf} from '@angular/common';
import {ApplicantDto} from '../../../dto/applicant';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ApplicationRequestModalComponent} from '../application-request-modal/application-request-modal.component';
import {RequestService} from '../../../service/request.service';

@Component({
  selector: 'app-job-details-user',
  imports: [
    NgIf,
    DatePipe
  ],
  templateUrl: './job-details-user.component.html',
  standalone: true,
  styleUrl: './job-details-user.component.css'
})
export class JobDetailsUserComponent implements OnInit {

  job: Job = {
    id: 0,
    title: '',
    description: '',
    client: '',
    city: '',
    location: '',
    storeNumber: '',
    startDate: new Date(),
    endDate: new Date(),
    shiftStart: '',
    shiftEnd: '',
    personInCharge: '',
    personInChargeNumber: '',
    totalSlots: 0,
    applicantsCount: 0
  }

  applicant: ApplicantDto | null = null;
  isApplied = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private jobService: JobService,
              private applicantService: ApplicantService,
              private applicationRequestService: RequestService,
              private modal: NgbModal) { }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    if (id) {
      this.jobService.getById(id).subscribe({
        next: job => {
          this.job = job;
          this.applicantService.getAppliedJobs().subscribe({
            next: appliedJobs => {
              this.isApplied = appliedJobs.some(job => job.id === id);
            }
          });
        }
      });

      this.applicantService.getByUser().subscribe({
        next: applicant => {
          this.applicant = applicant;
        },
        error: () => {
          this.applicant = null;
        }
      });
    }
  }

  availableSlots(): number {
    return this.job.totalSlots - this.job.applicantsCount;
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  applyForJob(): void {
    this.router.navigate(['/apply/job', this.job.id]);
  }

  requestToApply(): void {
    const requestModal = this.modal.open(ApplicationRequestModalComponent);
    requestModal.closed
      .subscribe((message: string) => {
        if (!message) return;

        this.applicationRequestService.create(this.job.id!, this.applicant!.id, message, 'APPLY')
          .subscribe({
            next: () => {
              alert('Your request to apply has been sent.')
            },
            error: err => {
              alert('Failed to send request: ' + err?.error)
            }
          });
    });
  }

  requestToUnapply(): void {
    const requestModal = this.modal.open(ApplicationRequestModalComponent);
    requestModal.closed
      .subscribe((message: string) => {
        if (!message) return;

        this.applicationRequestService.create(this.job.id!, this.applicant!.id, message, 'UNAPPLY')
          .subscribe({
            next: () => {
              alert('Your request to unapply has been sent.')
            },
            error: err => {
              alert('Failed to send request: ' + err?.error)
            }
          });
      });
  }

  canApply(): boolean {
    return !!this.applicant && (this.applicant.rank ?? 0) >= 1;
  }
}
