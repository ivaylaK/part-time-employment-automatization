
export interface Notification {
  id: number;
  message: string;
  created: string;
  read: boolean;

  jobId: number | null;
  jobTitle?: string;
  jobClient?: string;
  jobCity?: string;
  jobLocation?: string;
  jobStartDate?: string;
  jobEndDate?: string;
}
