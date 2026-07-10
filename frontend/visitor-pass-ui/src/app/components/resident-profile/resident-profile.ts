import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { Resident } from '../../model/Resident';
import { ResidentService } from '../../services/resident-service';
import { AuthService } from '../../services/auth-service';
import { Visitor } from '../../model/Visitor';
import { VisitorService } from '../../services/visitor-service';
import { PassService } from '../../services/pass-service';

@Component({
  selector: 'app-resident-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './resident-profile.html',
  styleUrl: './resident-profile.css',
})
export class ResidentProfile implements OnInit {

  resident?: Resident;
  isLoading = true;
  profileExists = false;
  visitors: Visitor[] = [];
  showVisitors = false;

  constructor(
    private residentService: ResidentService,
    private authService: AuthService,
    private router: Router,
    private visitorService: VisitorService,
    private passService: PassService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    const userId = this.authService.getAuthenticatedUserId();
    if (!userId) {
      this.isLoading = false;
      return;
    }
    this.residentService.getResidentProfile(userId).subscribe({
      next: (resident) => {
        this.resident = resident;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  editProfile() {
    if (!this.resident) {
      console.log("Resident is null");
      return;
    }
    this.router.navigate(['/edit-resident', this.resident.id]);
  }

  addVisitor() {
    this.router.navigate(['/add-visitor']);
  }

  viewAllPastVisitors() {
    this.visitorService.getMyVisitors().subscribe({
      next: (data) => {
        this.visitors = data;
        this.visitors.forEach(visitor => {
          this.passService.getPassForVisitor(visitor.id!).subscribe({
            next: () => {
              visitor.hasPass = true;
              this.cdr.detectChanges();
            },
            error: () => {
              visitor.hasPass = false;
              this.cdr.detectChanges();
            }
          });
        });
        this.showVisitors = true;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.cdr.detectChanges();
      }
    });
  }

  generatePass(visitorId: string) {
    this.visitorService.generatePass(visitorId).subscribe({
      next: () => {
        alert("Pass generation started...");
        // Wait for Kafka consumer to generate the pass
        setTimeout(() => {
          this.passService.getPassForVisitor(visitorId).subscribe({
            next: (pass) => {
              const visitor = this.visitors.find(v => v.id === visitorId);
              if (visitor) {
                visitor.hasPass = true;
              }
              this.router.navigate(['/create-pass', pass.passId]);
              this.cdr.detectChanges();
            },
            error: () => {
              alert("Pass is still being generated. Please try again in a moment.");
              this.cdr.detectChanges();
            }
          });
        }, 2000);
      },
      error: (err) => {
        console.error(err);
        alert(err.error || "Unable to publish Generate Pass request.");
        this.cdr.detectChanges();
      }
    });
  }

  viewPass(visitorId: string) {
    this.passService.getPassForVisitor(visitorId).subscribe({
      next: (pass) => {
        console.log("Pass:", pass);
        this.router.navigate(['/create-pass', pass.passId]);
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        alert("QR Pass not generated yet.");
        this.cdr.detectChanges();
      }
    });
  }
}