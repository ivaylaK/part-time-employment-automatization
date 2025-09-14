export interface Request {

  id: number;

  applicantId: number;
  applicantFirstName: string;
  applicantLastName: string;
  applicantNumber: string;
  applicantCity: string;
  applicantRank?: number;

  jobId: number;
  jobTitle: string;
  jobClient: string;
  jobCity: string;
  jobLocation: string;
  jobStoreNumber: string;
  jobStartDate: string;
  jobEndDate: string;

  message: string;
  sent: string;
  type: 'APPLY' | 'UNAPPLY';
}
