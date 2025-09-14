
export interface Job {
  id?: number;

  title: string;
  description: string;
  client: string;
  city: string;
  location: string;
  storeNumber: string;

  startDate: Date;
  endDate: Date;
  shiftStart: string;
  shiftEnd: string;

  personInCharge: string;
  personInChargeNumber: string;

  totalSlots: number;
  applicantsCount: number;
}

export interface JobDto {
  id: number;

  title: string;
  description: string;
  client: string;
  city: string;
  location: string;
  storeNumber: string;

  startDate: Date;
  endDate: Date;
  shiftStart: string;
  shiftEnd: string;

  personInCharge: string;
  personInChargeNumber: string;

  totalSlots: number;
  applicantsCount: number;
}

export interface JobSearch {
  title?: string;

  client?: string;
  city?: string;
  location?: string;

  earliestDate?: string;
  latestDate?: string;
}
