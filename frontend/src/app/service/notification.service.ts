import {environment} from '../environment/environment';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Notification} from '../dto/notification';

const baseUrl = environment.backendUrl + '/notifications';

@Injectable({ providedIn: 'root' })
export class NotificationService {

  constructor(private http: HttpClient) {}

  create(notification: Notification): Observable<Notification> {
    return this.http.post<Notification>(baseUrl, notification);
  }

  getAll(): Observable<Notification[]> {
    return this.http.get<Notification[]>(baseUrl);
  }

  markOneRead(id: number): Observable<void> {
    return this.http.post<void>(`${baseUrl}/markOneRead/${id}`, {});
  }

  markAllRead(): Observable<void> {
    return this.http.post<void>(`${baseUrl}/markAllRead`, {});
  }

  unreadNotificationCount(): Observable<number> {
    return this.http.get<number>(`${baseUrl}/count`);
  }

  delete(id: number): Observable<Notification> {
    return this.http.delete<Notification>(`${baseUrl}/${id}`);
  }
}
