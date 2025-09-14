import {Injectable} from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  GuardResult,
  MaybeAsync,
  Router
} from '@angular/router';
import {JobInvitationService} from '../service/job-invitation.service';
import {catchError, of, tap} from 'rxjs';
import {MatSnackBar} from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class ApplicationGuard implements CanActivate {
  constructor(private jobInvitationService: JobInvitationService,
              private router: Router,
              private snackBar: MatSnackBar) {
  }

  canActivate(route: ActivatedRouteSnapshot): MaybeAsync<GuardResult> {
    const token = route.queryParams['token'];

    if (!token) {
      this.invalidToken();
      return of(false);
    }

    return this.jobInvitationService.validateInvitationToken(token).pipe(
      tap(isValid => {
        if (!isValid) {
          this.invalidToken();
        }
      }),
      catchError(() => {
        this.invalidToken();
        return of(false);
      })
    );
  }

  private invalidToken(): void {
    this.router.navigate(['/jobs']);
    this.snackBar.open('Invalid or expired link. Please request a new one.', 'Close', {duration: 5000});
  }
}
