import { Component } from '@angular/core';
import {AuthenticationService} from '../../service/authentication.service';
import {Router} from '@angular/router';
import {FormsModule, NgForm} from '@angular/forms';
import {NgIf} from '@angular/common';

// @ts-ignore
@Component({
  selector: 'app-login',
  imports: [
    FormsModule,
    NgIf
  ],
  templateUrl: './login.component.html',
  standalone: true,
  styleUrl: './login.component.css'
})
export class LoginComponent {

  protected email = '';
  protected password = '';
  protected confirmPassword = '';
  protected firstName = '';
  protected lastName = '';
  protected city = '';
  protected phoneNumber = '';
  protected errorMessage = '';
  protected privacyPolicyAccepted = false;

  mode: 'signup' | 'login' = 'login';
  showPassword: boolean = false;

  constructor(private authenticationService: AuthenticationService, private router: Router) {
  }

  public get header(): string {
    switch (this.mode) {
      case 'signup':
        return 'Sign up';
      case 'login':
        return 'Log in'
      default:
        return '?';
    }
  }

  public isFormValid(): boolean {
    const emailValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email);
    const passwordValid = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{12,}$/.test(this.password);
    const namesValid = this.firstName.trim().length > 0 && this.lastName.trim().length > 0;
    const numberValid = /^\d{9}$/.test(this.phoneNumber);
    const privacyValid = this.privacyPolicyAccepted;

    if (this.mode === 'login') return emailValid && passwordValid
    else return emailValid && passwordValid && namesValid && numberValid && privacyValid;
  }

  public get submitButtonText(): string {
    switch (this.mode) {
      case 'signup':
        return 'Sign up';
      case 'login':
        return 'Log in';
      default:
        return '?';
    }
  }

  public switchMode(): void {
    if (this.mode === 'signup') this.mode = 'login';
    else this.mode = 'signup';
  }

  public get switchButtonText(): string {
    switch (this.mode) {
      case 'signup':
        return 'Log into your account';
      case 'login':
        return 'Create an account';
      default:
        return '?';
    }
  }

  public onSubmit(form: NgForm): void {
    if (form.valid && this.isFormValid()) {
      switch (this.mode) {
        case 'signup':
          this.authenticationService.signup(
            this.email,
            this.password,
            this.firstName,
            this.lastName,
            this.formatPhoneNumber(this.phoneNumber),
            this.city,
            this.privacyPolicyAccepted
          ).subscribe({
            next: () => {
              this.mode = 'login';
            },
            error: (e) => {
              this.errorMessage = "Registration failed.";
              console.error(e);
            }
          });
          break;
        case 'login':
          this.authenticationService.login(this.email, this.password).subscribe({
            next: (token: string) => {
              this.authenticationService.setToken(token);

              const role = this.authenticationService.getRole();

              if (role === "ADMIN") {
                this.router.navigate(['/admin/jobs']);
              } else if (role === "USER") {
                this.router.navigate(['/dashboard']);
              } else {
                this.router.navigate(['/']);
              }
            },
            error: (e) => {
              this.errorMessage = "Invalid username or password!"
              console.error(e);
            }
          });
          break;
      }
    }
  }

  private formatPhoneNumber(phoneNumber: string): string {
    phoneNumber = phoneNumber.trim().replace(/^(\+?359)?0*/, '');
    return '+359' + phoneNumber;
  }
}
