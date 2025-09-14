import {environment} from '../environment/environment';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Applicant, ApplicantDto, ApplicantSearch} from '../dto/applicant';
import {JobDto} from '../dto/job';

const baseUrl = environment.backendUrl + '/applicants';

@Injectable({
  providedIn: 'root'
})
export class ApplicantService {

  constructor(private http: HttpClient) {
  }

  create(applicant: Applicant): Observable<Applicant> {
    return this.http.post<Applicant>(baseUrl, applicant);
  }

  addBlockedClient(id: number, client: string): Observable<string> {
    return this.http.post<string>(`${baseUrl}/${id}/blocked-clients`, {}, {
      params: new HttpParams().set('client', client)
    });
  }

  getAll(): Observable<Applicant[]> {
    return this.http.get<Applicant[]>(baseUrl);
  }

  getAllByFilter(filter: ApplicantSearch): Observable<Applicant[]> {
    let params = new HttpParams();

    if (filter.firstName?.trim()) {
      params = params.append('firstName', filter.firstName as string);
    }
    if (filter.lastName?.trim()) {
      params = params.append('lastName', filter.lastName as string);
    }
    if (filter.number?.trim()) {
      params = params.append('number', filter.number as string);
    }
    if (filter.city?.trim()) {
      params = params.set('city', filter.city.trim());
    }
    if (filter.rank !== undefined && filter.rank !== null) {
      params = params.set('rank', String(filter.rank));
    }

    return this.http.get<Applicant[]>(baseUrl, {params});
  }

  getById(id: number): Observable<Applicant> {
    return this.http.get<Applicant>(`${baseUrl}/${id}`);
  }

  getByUser() {
    return this.http.get<ApplicantDto>(`${baseUrl}/self`);
  }

  getBlockedClients(id: number): Observable<string[]> {
    return this.http.get<string[]>(`${baseUrl}/${id}/blocked-clients`);
  }

  getAppliedJobs(): Observable<JobDto[]> {
    return this.http.get<JobDto[]>(`${baseUrl}/dashboardApplied`);
  }

  getAvailableJobs(): Observable<JobDto[]> {
    return this.http.get<JobDto[]>(`${baseUrl}/dashboardAvailable`);
  }

  exportApplicantsToExcel(): Observable<Blob> {
    return this.http.get(`${baseUrl}/export`, { responseType: 'blob' });
  }

  update(applicant: Applicant): Observable<Applicant> {
    return this.http.put<Applicant>(`${baseUrl}/${applicant.id}`, applicant);
  }

  updateRank(id: number, rank: number): Observable<void> {
    return this.http.put<void>(`${baseUrl}/${id}/rank`, rank);
  }

  removeBlockedClient(id: number, client: string): Observable<string> {
    return this.http.delete<string>(`${baseUrl}/${id}/blocked-clients`, {
      params: new HttpParams().set('client', client)
    });
  }

  delete(id: number): Observable<Applicant> {
    return this.http.delete<Applicant>(`${baseUrl}/${id}`);
  }
}
