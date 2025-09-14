import {environment} from '../environment/environment';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {JobApplication} from '../dto/job-application';

const baseUrl = environment.backendUrl + '/jobs/applications';

@Injectable({
  providedIn: 'root'
})
export class JobApplicationService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<JobApplication[]> {
    return this.http.get<JobApplication[]>(baseUrl);
  }

  deleteJobApplication(jobId: number, applicantId: number): Observable<void> {
    return this.http.delete<void>(`${baseUrl}/delete/${jobId}/${applicantId}`);
  }

  exportApplicationsToExcel(): Observable<Blob> {
    return this.http.get(`${baseUrl}/export`, { responseType: 'blob' });
  }
}
