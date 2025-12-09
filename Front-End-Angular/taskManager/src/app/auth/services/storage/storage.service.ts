import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

const TOKEN="token";
const USER="user";

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  private static hasToken(): boolean {
    return !!localStorage.getItem(TOKEN);
  }

  private loggedIn = new BehaviorSubject<boolean>(StorageService.hasToken());
  loginStatus$ = this.loggedIn.asObservable();

  constructor() {}

  getToken(): string | null {
  return localStorage.getItem(TOKEN);
}

  // Save token & notify components
  saveToken(token: string) {
    localStorage.setItem(TOKEN, token);
    this.loggedIn.next(true);
  }

  saveUser(user: any) {
    localStorage.setItem(USER, JSON.stringify(user));
  }

  getUser() {
    const data = localStorage.getItem(USER);
    return data ? JSON.parse(data) : null;
  }

  getUserRole(): string {
    const user = this.getUser();
    return user?.role ?? '';
  }

  isAdminLoggedIn(): boolean {
    return this.getUserRole() === 'ADMIN';
  }

  isEmployeeLoggedIn(): boolean {
    return this.getUserRole() === 'EMPLOYEE';
  }

  logout() {
    localStorage.removeItem(TOKEN);
    localStorage.removeItem(USER);
    this.loggedIn.next(false);
  }
}
