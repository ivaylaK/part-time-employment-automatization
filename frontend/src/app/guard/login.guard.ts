import {Injectable} from '@angular/core';
import {AuthenticationService} from '../service/authentication.service';
import {
  CanActivate,
  Router
} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {
  constructor(private authenticationService: AuthenticationService,
              private router: Router) {
  }

  canActivate(): boolean {
    if (!this.authenticationService.isLoggedIn()) {
      this.router.navigate(['/authentication/login']);
      return false;
    }
    return true;
  }
}
