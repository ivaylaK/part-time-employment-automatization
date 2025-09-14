import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {saveAs} from 'file-saver';
import {JobApplication} from '../../../dto/job-application';
import {JobApplicationService} from '../../../service/job-application.service';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-job-applications',
  imports: [
    NgForOf
  ],
  templateUrl: './job-applications.component.html',
  standalone: true,
  styleUrl: './job-applications.component.css'
})
export class JobApplicationsComponent implements OnInit {

  jobApplications: JobApplication[] = [];

  constructor(private service: JobApplicationService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.getApplications();
  }

  getApplications(): void {
    this.service.getAll()
      .subscribe({
        next: applications => {
          this.jobApplications = applications;
        },
        error: err => console.error('Error fetching job applications.', err)
      })
  }

  exportApplicationsToExcel(): void {
    this.service.exportApplicationsToExcel().subscribe({
      next: (response) => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        saveAs(blob, 'job-applications.xlsx');
      },
      error: err => console.error('Failed to export job applications', err)
    });
  }
}
