import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import {provideHttpClient, withInterceptors} from '@angular/common/http';

import { routes } from './app.routes';
import {authenticationInterceptor} from './interceptor/authentication.interceptor';
import {provideNgcCookieConsent} from 'ngx-cookieconsent';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([authenticationInterceptor])),

    provideNgcCookieConsent({
      cookie: {
        domain: 'localhost'
      },
      palette: {
        popup: {
          background: '#000'
        },
        button: {
          background: '#f1d600'
        }
      },
      theme: 'edgeless',
      type: 'opt-out'
    })
  ]
};
