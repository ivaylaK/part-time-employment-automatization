import {Component, OnDestroy, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {
  NgcCookieConsentService,
  NgcInitializationErrorEvent,
  NgcInitializingEvent, NgcNoCookieLawEvent,
  NgcStatusChangeEvent
} from 'ngx-cookieconsent';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  standalone: true,
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'frontend';

  private subscriptions: Subscription[] = [];

  constructor(private cookieService: NgcCookieConsentService) {}

  ngOnInit() {
    this.subscriptions.push(
      this.cookieService.popupOpen$.subscribe(() => {
        console.log('Popup opened');
      }),
      this.cookieService.popupClose$.subscribe(() => {
        console.log('Popup closed');
      }),
      this.cookieService.initializing$.subscribe((event: NgcInitializingEvent) => {
        console.log(`Initializing: ${JSON.stringify(event)}`);
      }),
      this.cookieService.initialized$.subscribe(() => {
        console.log('Initialized');
      }),
      this.cookieService.initializationError$.subscribe((event: NgcInitializationErrorEvent) => {
        console.error(`Initialization error: ${event.error?.message}`);
      }),
      this.cookieService.statusChange$.subscribe((event: NgcStatusChangeEvent) => {
        console.log(`Status change: ${event.status}`);
      }),
      this.cookieService.revokeChoice$.subscribe(() => {
        console.log('Choice revoked');
      }),
      this.cookieService.noCookieLaw$.subscribe((event: NgcNoCookieLawEvent) => {
        console.log('No cookie law for this region');
      })
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach((s) => s.unsubscribe());
  }
}
