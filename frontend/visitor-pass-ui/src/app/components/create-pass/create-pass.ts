import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Pass } from '../../model/Pass';
import { PassService } from '../../services/pass-service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-create-pass',
  imports: [CommonModule, RouterLink],
  templateUrl: './create-pass.html',
  styleUrl: './create-pass.css',
})
export class CreatePass implements OnInit {

  pass?: Pass;
  isLoading = true;

  constructor(
    private passService: PassService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {

    const passId = this.route.snapshot.paramMap.get('id');

    console.log("Route passId:", passId);

    if (!passId) {
      this.isLoading = false;
      this.cdr.detectChanges();
      return;
    }

    this.passService.getPassById(passId).subscribe({
      next: (data) => {
        console.log("Fetched pass:", data);
        this.pass = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("getPassById failed", err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  downloadQRCode() {
    if (this.pass?.qrImage) {
      const link = document.createElement('a');
      link.href = this.pass.qrImage;
      link.download = 'visitor-qr.png';
      link.click();
    }
  }

  cancelPass() {
    if (!this.pass?.passId) {
      return;
    }
    this.passService.cancelPass(this.pass.passId).subscribe({
      next: (updatedPass) => {
        this.pass = updatedPass;
        alert('Pass cancelled successfully.');
        this.router.navigate(['/resident-profile']);
      },
      error: (err) => {
        console.error(err);
        alert('Already cancelled or cannot cancel this pass.');
      }
    });
  }
}

