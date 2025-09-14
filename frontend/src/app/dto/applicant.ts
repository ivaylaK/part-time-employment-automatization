import {Job} from './job';

export interface Applicant {
  id?: number;

  firstName: string;
  lastName: string;

  number: string;
  city: string;

  jobsApplied: Job[];
  rank?: number;
}

export interface ApplicantDto {
  id: number;

  firstName: string;
  lastName: string;

  number: string;
  city: string;

  jobsApplied: Job[];
  rank?: number;
}

export interface ApplicantSearch {
  firstName?: string;
  lastName?: string;

  number?: string;
  city?: string;
  rank?: number;
}
