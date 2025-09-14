import {Component, OnInit} from '@angular/core';
import {Job} from '../../../dto/job';
import {JobService} from '../../../service/job.service';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {FormsModule, NgForm} from '@angular/forms';
import {Observable} from 'rxjs';

export enum JobCreateEditMode {
  create,
  edit
}

@Component({
  selector: 'app-job-create',
  imports: [
    FormsModule,
    RouterLink
  ],
  templateUrl: './job-create-edit.component.html',
  standalone: true,
  styleUrl: './job-create-edit.component.css'
})
export class JobCreateEditComponent implements OnInit {

  mode: JobCreateEditMode = JobCreateEditMode.create;
  job: Job = {
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

  constructor(private service: JobService,
              private router: Router,
              private route: ActivatedRoute) {
  }

  public get header(): string {
    switch (this.mode) {
      case JobCreateEditMode.create:
        return 'Create New Job';
      case JobCreateEditMode.edit:
        return 'Edit Job'
      default:
        return '?';
    }
  }

  public get submitButtonText(): string {
    switch (this.mode) {
      case JobCreateEditMode.create:
        return 'Create';
      case JobCreateEditMode.edit:
        return 'Edit';
      default:
        return '?';
    }
  }

  get modeIsEdit(): boolean {
    return this.mode === JobCreateEditMode.edit;
  }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.mode = data['mode'];
    });

    if (this.modeIsEdit) {
      const id = Number(this.route.snapshot.paramMap.get('id'));

      if (id != undefined) {
        this.service.getById(id).subscribe({
          next: job => {
            this.job = job;
          },
          error: error => {
            console.error('Error loading job', error);
          }
        });
      } else {
        console.error("Unknown job id!")
      }
    }
  }

  public onSubmit(form: NgForm): void {
    if (form.valid) {
      let observable: Observable<Job>;
      switch (this.mode) {
        case JobCreateEditMode.create:
          observable = this.service.create(this.job);
          break;
        case JobCreateEditMode.edit:
          observable = this.service.update(this.job);
          break;
        default:
          console.error('Unknown JobCreateEditMode', this.mode);
          return;
      }
      observable.subscribe({
        next: () => {
          this.router.navigate(['/jobs'])
        },
        error: error => {
          console.error('Error saving job', error);
        }
      });
    }
  }

  delete(): void {
    if (this.job.id == null) {
      return;
    }
    this.service.delete(this.job.id).subscribe({
      next: () => {
        this.router.navigate(['/jobs'])
      },
      error: error => {
        console.error('Error deleting job', error);
      }
    });
  }
}
