import {Injectable} from '@angular/core';
import {environment} from '../environment/environment';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {jwtDecode} from 'jwt-decode';
import {JwtPayload} from '../dto/jwt-payload';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: "root"
})
export class AuthenticationService {

  private token = "access_token";

  private readonly baseUrl = environment.backendUrl + '/authentication';

  constructor(private http: HttpClient, private router: Router) {
  }

  getToken(): string | null {
    return localStorage.getItem(this.token);
  }

  setToken(token: string) {
    localStorage.setItem(this.token, token);
  }

  login(email: string, password: string) {
    return this.http.post(`${this.baseUrl}/login`, {email, password}, {responseType: 'text'});
  }

  signup(email: string, password: string, firstName: string, lastName: string, number: string, city: string, privacyPolicyAccepted: boolean): Observable<any> {
    if (!privacyPolicyAccepted) {
      throw new Error("Cannot continue without accepting the privacy policy!");
    }

    const data = {
      email,
      password,
      role: 'USER',
      firstName,
      lastName,
      number,
      city
    }
    return this.http.post(`${this.baseUrl}/signup`, data, {responseType: 'text'});
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;
    try {
      const decoded = jwtDecode<JwtPayload>(token);
      return Date.now() < decoded.exp * 1000;
    } catch {
      return false;
    }
  }

  logout() {
    localStorage.removeItem(this.token);
    this.router.navigate(['authentication/login']);
  }

  getRole(): string | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      const decoded = jwtDecode<JwtPayload>(token);
      console.log();
      return decoded.role;
    } catch {
      return null;
    }
  }

  isUser(): boolean {
    return this.getRole() === "USER";
  }

  isAdmin(): boolean {
    return this.getRole() === "ADMIN";
  }
}
