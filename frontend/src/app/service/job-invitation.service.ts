import {environment} from '../environment/environment';
import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ChosenDays} from '../dto/chosen-days';

const baseUrl = environment.backendUrl + '/jobs/invitation';

@Injectable({
  providedIn: 'root'
})
export class JobInvitationService {

  constructor(private http: HttpClient) {
  }

  getInvitationLink(jobId: number, contact: string): Observable<string> {
    return this.http.get(`${baseUrl}/${jobId}?contact=${contact}`, {responseType: 'text'});
  }

  validateInvitationToken(token: string): Observable<boolean> {
    return this.http.get<boolean>(`${baseUrl}/validate?token=${token}`);
  }

  applyToJobWithToken(token: string, chosenDays: ChosenDays): Observable<string> {
    return this.http.post(`${baseUrl}/apply?token=${token}`, chosenDays, { responseType: 'text' });
  }

  applyToJobThroughProfile(id: number, chosenDays: ChosenDays): Observable<string> {
    return this.http.post(`${baseUrl}/apply/${id}`, chosenDays, { responseType: 'text' });
  }

  addApplicantAsAdmin(id: number, applicantId: number, chosenDays: ChosenDays): Observable<string> {
    const params = new HttpParams().set('applicantId', String(applicantId));
    return this.http.post(`${baseUrl}/admin/apply/${id}`, chosenDays, { params, responseType: 'text' });
  }
}
