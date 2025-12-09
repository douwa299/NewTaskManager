import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { StorageService } from '../../services/storage/storage.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
   loginForm!:FormGroup;
   hidePassword=true;

  constructor(private fb:FormBuilder,
     private authService:AuthService,
    private snackbar : MatSnackBar,
    private router:Router,
      private storageService: StorageService

  ){
    this.loginForm = this.fb.group({
      email:[null,[Validators.required]],
      password:[null,[Validators.required]],
    })
  }

    togglePasswordVisibility(){
        this.hidePassword=  !this.hidePassword;
    }

    onSubmit() {
  if (this.loginForm.invalid) return;

  this.authService.login(this.loginForm.value).subscribe({
    next: (res) => {
      if (res.userId) {
        const user={
          id:res.userId,
          role:res.userRole,

        }
       this.storageService.saveUser(user);
      this.storageService.saveToken(res.jwt);


        if( this.storageService.isAdminLoggedIn()){
          this.router.navigateByUrl('/admin/dashboard');
        }else if( this.storageService.isEmployeeLoggedIn()){
          this.router.navigateByUrl('/employee/dashboard');
        }
        this.snackbar.open("Login successful", "Close", { duration: 5000 });
      } else {
        this.snackbar.open("Invalid Credentials Try again", "Close", {
          duration: 5000,
          panelClass: "error-snackbar",
        });
      }
    },

    error: () => {
      this.snackbar.open("Invalid Credentials Try again", "Close", {
        duration: 5000,
        panelClass: "error-snackbar",
      });
    }
  });
}



}
