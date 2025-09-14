import {environment} from '../environment/environment';
import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Request} from '../dto/request';
import {Applicant} from '../dto/applicant';

const baseUrl = environment.backendUrl + '/application-requests';

@Injectable({
  providedIn: 'root'
})
export class RequestService {

  constructor(private http: HttpClient) {
  }

  create(jobId: number, applicantId: number, message: string, type: 'APPLY' | 'UNAPPLY'): Observable<Request> {
    let params = new HttpParams()
                            .set('jobId', jobId.toString())
                            .set('applicantId', applicantId.toString())
                            .set('message', message)
                            .set('type', type);

    return this.http.post<Request>(baseUrl, {}, {params});
  }

  getAll(): Observable<Request[]> {
    return this.http.get<Request[]>(baseUrl);
  }

  approveAndDelete(id: number): Observable<void> {
    return this.http.post<void>(`${baseUrl}/approve/${id}`, {});
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${baseUrl}/${id}`);
  }
}
