import { Applicant } from './applicant';
import { Job } from './job';

export interface JobApplication {
  id: number;

  // job: Job;
  jobId: number;
  jobTitle: string;
  jobClient: string;
  jobCity: string;
  jobLocation: string;
  jobStoreNumber: string;
  jobDatePeriod: string;
  jobShiftPeriod: string;

  // applicant: Applicant;
  applicantId: number;
  applicantFullName: string;
  applicantNumber: string;
}
