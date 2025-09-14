import {environment} from '../environment/environment';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Job, JobDto, JobSearch} from '../dto/job';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Applicant} from '../dto/applicant';

const baseUrl = environment.backendUrl + '/jobs';

@Injectable({
  providedIn: 'root'
})
export class JobService {

  constructor(private http: HttpClient) {
  }

  create(job: Job): Observable<Job> {
    return this.http.post<Job>(baseUrl, job);
  }

  getAllByFilter(filter: JobSearch): Observable<JobDto[]> {
    let params = new HttpParams();

    if (filter.title?.trim()) {
      params = params.append('title', filter.title as string);
    }
    if (filter.client?.trim()) {
      params = params.append('client', filter.client as string);
    }
    if (filter.city?.trim()) {
      params = params.append('city', filter.city as string);
    }
    if (filter.location?.trim()) {
      params = params.append('location', filter.location as string);
    }
    if (filter.earliestDate?.trim()) {
      params = params.append('earliestDate', filter.earliestDate as string);
    }
    if (filter.latestDate?.trim()) {
      params = params.append('latestDate', filter.latestDate as string);
    }

    return this.http.get<JobDto[]>(baseUrl, {params})
  }

  getById(id: number): Observable<Job> {
    return this.http.get<Job>(`${baseUrl}/${id}`);
  }

  getApplicantsForJob(jobId: number): Observable<Applicant[]> {
    return this.http.get<Applicant[]>(`${baseUrl}/${jobId}/applicants`);
  }

  getAllClients(): Observable<string[]> {
    return this.http.get<string[]>(`${baseUrl}/clients`);
  }

  update(job: Job): Observable<Job> {
    return this.http.put<Job>(`${baseUrl}/${job.id}`, job);
  }

  delete(id: number): Observable<Job> {
    return this.http.delete<Job>(`${baseUrl}/${id}`);
  }
}
