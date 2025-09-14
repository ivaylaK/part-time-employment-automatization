import { inject } from '@angular/core';
import {
  HttpInterceptorFn
} from '@angular/common/http';
import {AuthenticationService} from '../service/authentication.service';

export const authenticationInterceptor: HttpInterceptorFn = (req, next) => {
  const authenticationService = inject(AuthenticationService);
  const token = authenticationService.getToken();

  console.log("Interceptor fired. Token:", token);

  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  return next(req);
};
