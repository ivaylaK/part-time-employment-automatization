import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {Notification} from '../../../dto/notification';
import {NotificationService} from '../../../service/notification.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-messages-modal-user',
  imports: [
    DatePipe,
    NgIf,
    NgForOf
  ],
  templateUrl: './messages-modal-user.component.html',
  standalone: true,
  styleUrl: './messages-modal-user.component.css'
})
export class MessagesModalUserComponent implements OnInit {

  @Input() notifications: Notification[] = [];
  selected: number | null = null;

  constructor(public modal: NgbActiveModal,
              private notificationService: NotificationService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.notificationService.getAll().subscribe({
      next: (notifications) => {
        console.log('Notifications:', notifications);
        this.notifications = notifications;
      }
    });
  }

  apply(notification: Notification): void {
    this.markOneRead(notification);
    this.modal.close(); // close modal before navigating
    this.router.navigate(['/dashboard/details', notification.jobId]);
  }

  markOneRead(notification: Notification): void {
    if (notification.read) return;

    this.selected = notification.id;
    this.notificationService.markOneRead(notification.id).subscribe({
      next: () => {
        notification.read = true;
        this.selected = null;
      },
      error: () => {
        this.selected = null;
      }
    });
  }

  markAllRead(): void {
    this.notificationService.markAllRead().subscribe({
      next: () => {
        this.notifications = this.notifications.map(n => ({ ...n, read: true }));
      }
    });
  }

  remove(id: number): void {
    this.selected = id;
    this.notificationService.delete(id).subscribe({
      next: () => {
        this.notifications = this.notifications.filter(r => r.id !== id);
        this.selected = null;
      },
      error: () => { this.selected = null; }
    });
  }
}
