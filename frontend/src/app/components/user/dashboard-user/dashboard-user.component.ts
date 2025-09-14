import { Component } from '@angular/core';
import {JobDto} from '../../../dto/job';
import {AuthenticationService} from '../../../service/authentication.service';
import {Router} from '@angular/router';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {ApplicantService} from '../../../service/applicant.service';
import {NotificationService} from '../../../service/notification.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {MessagesModalUserComponent} from '../messages-modal-user/messages-modal-user.component';

@Component({
  selector: 'app-profile-dashboard',
  imports: [
    DatePipe,
    NgForOf,
    NgIf
  ],
  templateUrl: './dashboard-user.component.html',
  standalone: true,
  styleUrl: './dashboard-user.component.css'
})
export class DashboardUserComponent {

  appliedJobs: JobDto[] = [];
  availableJobs: JobDto[] = [];
  selectedJob: JobDto | undefined;
  showAvailable: boolean = true;
  messagesCount = 0;

  constructor(private authenticationService: AuthenticationService,
              private applicantService: ApplicantService,
              private notificationService: NotificationService,
              private modal: NgbModal,
              private router: Router) { }

  ngOnInit(): void {
    this.refreshMessagesCount();
    this.getAppliedJobs();
    this.getAvailableJobs();
  }

  getAppliedJobs(): void {
    this.applicantService.getAppliedJobs()
      .subscribe({
        next: jobs => {
          this.appliedJobs = jobs;
        },
        error: err => {
          console.error('Error fetching applied jobs', err);
        }
      });
  }

  getAvailableJobs(): void {
    this.applicantService.getAvailableJobs()
      .subscribe({
        next: jobs => {
          this.availableJobs = jobs;
        },
        error: err => {
          console.error('Error fetching available jobs', err);
        }
      });
  }

  details(id: number): void {
    const jobs = [...this.availableJobs, ...this.appliedJobs];
    this.selectedJob = jobs.find(job => job.id == id);
    if (this.selectedJob == undefined || this.selectedJob.id == undefined) {
      return;
    } else {
      this.router.navigate(['/dashboard/details/', this.selectedJob.id]);
    }
  }

  openMessages(): void {
    const messagesModal = this.modal.open(MessagesModalUserComponent);
    messagesModal.closed.subscribe(() => this.refreshMessagesCount());
  }

  refreshMessagesCount(): void {
    this.notificationService.unreadNotificationCount().subscribe({
      next: notificationsCount => this.messagesCount = notificationsCount,
      error: () => this.messagesCount = 0
    });
  }
}
