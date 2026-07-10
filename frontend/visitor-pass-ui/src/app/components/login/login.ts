import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { LoginModel } from '../../model/Login';
import { AuthService } from '../../services/auth-service';
import { Router, RouterLink } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';
import { PLATFORM_ID, inject } from '@angular/core';

declare const google: any;

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements AfterViewInit {

  @ViewChild('loginForm')
  loginForm!: NgForm;

  loginInfo: LoginModel = { email: '', password: '' };
  errorMessage = '';
  private platformId = inject(PLATFORM_ID);


  constructor(private authService: AuthService, private readonly router: Router) { }

  ngAfterViewInit(): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }
    this.initializeGoogle();
  }

  initializeGoogle() {
    if (typeof google === 'undefined') {
      console.log('Google SDK not loaded');
      return;
    }

    const btn = document.getElementById('googleBtn');
    console.log('Button:', btn);

    if (!btn) {
      console.log('Button not found');
      return;
    }

    google.accounts.id.initialize({
      client_id: '871952614321-omeig6ks70egmjb6nljtjo7lna5q5bu9.apps.googleusercontent.com',
      callback: (response: any) => this.handleGoogleLogin(response)
    });

    btn.innerHTML = '';

    google.accounts.id.renderButton(btn, {
      theme: 'outline',
      size: 'large',
      text: 'signup_with',
      width: 350
    });

    console.log('Button rendered');
  }

  handleGoogleLogin(response: any) {
    this.authService.googleLogin(response.credential).subscribe({
      next: () => {
        const role = this.authService.getAuthenticatedUserRole();
        if (role === 'RESIDENT') {
          this.router.navigate(['/resident-profile']);
        } else if (role === 'GUARD') {
          this.router.navigate(['/guard-dashboard']);
        } else {
          this.errorMessage = 'Invalid user role.';
        }
      },
      error: () => {
        this.errorMessage = 'Google login failed.';
      }
    });
  }

  login() {
    if (this.loginForm.invalid) {
      Object.values(this.loginForm.controls).forEach(control => {
        control.markAsTouched();
      });
      return;
    }
    this.authService.login(this.loginInfo).subscribe({
      next: (res) => {
        const role = this.authService.getAuthenticatedUserRole();
        if (role === 'RESIDENT') {
          this.router.navigate(['/resident-profile']);
        } else {
          // this.router.navigate(['/guard-dashboard']);
        }
      },
      error: (err) => {
        this.errorMessage = "Invalid credentials.";
        setTimeout(() => {
          this.errorMessage = '';
        }, 5000);
      },

    });
  }
}
