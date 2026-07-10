import { Component, HostListener, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { Navbar } from "./components/navbar/navbar";
import { AuthService } from './services/auth-service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  constructor(public authService: AuthService, public router: Router) { }

  protected readonly title = signal('visitor-pass-ui');

  showNavbar(): boolean {
    return this.authService.isLoggedin();
  }

  @HostListener('window:beforeunload')
  clearSession() {
    this.authService.logout();
  }
}
