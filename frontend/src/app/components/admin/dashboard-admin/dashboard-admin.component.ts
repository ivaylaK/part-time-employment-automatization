import {Component, OnInit} from '@angular/core';
import {Job, JobDto, JobSearch} from '../../../dto/job';
import {JobService} from '../../../service/job.service';
import {Router, RouterLink} from '@angular/router';
import {debounceTime, Subject} from 'rxjs';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {NgbModal, NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {MessagesModalAdminComponent} from '../messages-modal-admin/messages-modal-admin.component';
import {RequestService} from '../../../service/request.service';

@Component({
  selector: 'app-jobs-dashboard',
  standalone: true,
  imports: [
    DatePipe,
    NgForOf,
    RouterLink,
    NgbModalModule,
    NgIf
  ],
  providers: [
    DatePipe
  ],
  templateUrl: './dashboard-admin.component.html',
  styleUrl: './dashboard-admin.component.css'
})
export class DashboardAdminComponent implements OnInit {

  jobs: JobDto[] = [];
  searchParams: JobSearch = {}
  searchChangeObservable: Subject<void> = new Subject<void>();

  selectedJob: Job | undefined;
  messagesCount = 0;

  constructor(private applicationRequestService: RequestService,
              private service: JobService,
              private router: Router,
              private modal: NgbModal) {
  }

  ngOnInit(): void {
    this.refreshMessagesCount();
    this.getJobs();
    this.searchChangeObservable.pipe(debounceTime(200))
      .subscribe({next: () => this.getJobs()});
  }

  getJobs(): void {
    this.service.getAllByFilter(this.searchParams)
      .subscribe({
        next: jobs => {
          this.jobs = jobs;
        },
        error: err => console.error('Error fetching jobs.', err)
      })
  }

  searchChanged(): void {
    this.searchChangeObservable.next();
  }

  details(id: number) {
    this.selectedJob = this.jobs.find(
      job => job.id == id
    )
    if (this.selectedJob == undefined || this.selectedJob.id == undefined) {
      return;
    } else {
      this.router.navigate(['admin/jobs/details', this.selectedJob.id]);
    }
  }

  edit(id: number) {
    this.selectedJob = this.jobs.find(
      job => job.id == id
    )
    if (this.selectedJob == undefined || this.selectedJob.id == undefined) {
      return;
    } else {
      this.router.navigate(['admin/jobs/edit', this.selectedJob.id]);
    }
  }

  delete(id: number) {
    this.selectedJob = this.jobs.find(
      job => job.id == id
    )
    if (this.selectedJob === undefined) {
      return;
    } else {
      this.service.delete(id).subscribe({
        next: () => {
          this.jobs = this.jobs.filter(job => job.id != id);
          console.log(this.jobs);
          this.router.navigate(['admin/jobs'])
        },
        error: error => {
          console.error('Error deleting job', error);
        }
      });
    }
  }

  openMessages(): void {
    const messagesModal = this.modal.open(MessagesModalAdminComponent);
    messagesModal.closed.subscribe(() => this.refreshMessagesCount());
  }

  refreshMessagesCount(): void {
    this.applicationRequestService.getAll().subscribe({
      next: requests => this.messagesCount = requests.length,
      error: () => this.messagesCount = 0
    });
  }
}
