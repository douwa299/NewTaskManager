import { Component, OnInit } from '@angular/core';
import { StorageService } from './auth/services/storage/storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  isEmployeeLoggedIn = false;
  isAdminLoggedIn = false;

  constructor(
    private router: Router,
    private storageService: StorageService
  ) {}

  ngOnInit() {
    // Monitor login changes automatically
    this.storageService.loginStatus$.subscribe(() => {
      this.isEmployeeLoggedIn = this.storageService.isEmployeeLoggedIn();
      this.isAdminLoggedIn = this.storageService.isAdminLoggedIn();
    });

    // Also update when navigation occurs
    this.router.events.subscribe(() => {
      this.isEmployeeLoggedIn = this.storageService.isEmployeeLoggedIn();
      this.isAdminLoggedIn = this.storageService.isAdminLoggedIn();
    });
  }

  logout() {
    this.storageService.logout();
    this.router.navigateByUrl("/login");
  }
}
