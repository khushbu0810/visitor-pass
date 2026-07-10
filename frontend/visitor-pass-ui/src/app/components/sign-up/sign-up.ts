import { AfterViewInit, Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { User } from '../../model/User';
import { AuthService } from '../../services/auth-service';
import { isPlatformBrowser } from '@angular/common';
import { PLATFORM_ID, inject } from '@angular/core';

declare const google: any;

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './sign-up.html',
  styleUrl: './sign-up.css',
})
export class SignUp implements AfterViewInit {

  successMessage = '';
  errorMessage = '';
  confirmPassword = '';
  selectedGoogleRole = 'RESIDENT';

  user: User = {
    username: '',
    email: '',
    password: '',
    accountStatus: true,
    role: 'RESIDENT'
  };

  private platformId = inject(PLATFORM_ID);

  constructor(
    private authService: AuthService,
    private router: Router,
  ) { }

  ngAfterViewInit(): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }
    this.initializeGoogle();
  }

  initializeGoogle() {
    if (typeof google === 'undefined') {
      console.log('Google SDK not loaded yet');
      return;
    }
    google.accounts.id.initialize({
      client_id: '871952614321-omeig6ks70egmjb6nljtjo7lna5q5bu9.apps.googleusercontent.com',
      callback: (response: any) => this.handleGoogleLogin(response)
    });
    google.accounts.id.renderButton(
      document.getElementById('googleBtn'),
      {
        theme: 'outline',
        size: 'large',
        text: 'signup_with',
        width: 350
      }
    );

  }

  handleGoogleLogin(response: any) {
    this.authService.googleSignUp(response.credential, this.user.role!).subscribe({
      next: () => {
        const role = this.authService.getAuthenticatedUserRole();
        if (role === 'RESIDENT') {
          this.router.navigate(['/resident-profile']);
        } else if (role === 'GUARD') {
          // this.router.navigate(['/guard-dashboard']);
        } else {
          this.errorMessage = 'Invalid user role.';
        }
      },
      error: () => {
        this.errorMessage = 'Google login failed.';
      }
    });
  }

  register(form: NgForm) {
    if (form.invalid) {
      Object.values(form.controls).forEach(control => control.markAsTouched());
      this.errorMessage = 'All fields are required.';
      return;
    }
    if (this.user.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }

    this.authService.register(this.user).subscribe({
      next: () => {
        this.errorMessage = '';
        this.successMessage = 'Registration successful! Redirecting to login...';

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: () => {
        this.successMessage = '';
        this.errorMessage = 'Registration failed. Please try again.';
      }
    });
  }
}

