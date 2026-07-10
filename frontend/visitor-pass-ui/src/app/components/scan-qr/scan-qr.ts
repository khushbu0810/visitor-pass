import { Component, OnDestroy, OnInit } from '@angular/core';
import { BrowserMultiFormatReader, IScannerControls } from '@zxing/browser';
import { PassService } from '../../services/pass-service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-scan-qr',
  standalone: true,
  imports: [],
  templateUrl: './scan-qr.html',
  styleUrl: './scan-qr.css',
})
export class ScanQr implements OnInit, OnDestroy {
  private codeReader = new BrowserMultiFormatReader();
  isScanning = true;
  private controls?: IScannerControls;
  expectedVisitorId = '';

  error = '';

  constructor(
    private passService: PassService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {

    this.expectedVisitorId =
      this.route.snapshot.queryParamMap.get('visitorId') ?? '';

    console.log("Expected Visitor:", this.expectedVisitorId);

    this.startScanner();
  }

  startScanner() {
    this.codeReader.decodeFromVideoDevice(
      undefined,
      'video',
      (result, err) => {
        if (result) {
          this.isScanning = false;
          this.controls?.stop();
          this.verifyQRCode(result.getText());
        }
      }
    ).then((controls) => {
      this.controls = controls;
    });
  }

  verifyQRCode(qrCode: string) {
    this.passService.verifyPass(qrCode).subscribe({
      next: (pass) => {
        if (pass.visitorId !== this.expectedVisitorId) {
          alert("This QR code belongs to another visitor.");
          this.error = "This QR code belongs to another visitor.";
          this.isScanning = true;
          this.startScanner();
          return;
        }

        this.router.navigate(['/guard-dashboard'],
          {
            queryParams: {
              verifiedVisitor: pass.visitorId
            }
          }
        );
      },
      error: (err) => {
        console.error(err);
        switch (err.status) {
          case 404:
            this.error = "Invalid QR Code.";
            alert(this.error);
            break;

          case 400: {
            const message = err.error?.message || err.error || "";

            if (message.includes("already been used")) {
              this.error = "This visitor's pass has already been used.";
            }
            else if (message.includes("cancelled")) {
              this.error = "This visitor's pass has been cancelled.";
            }
            else if (message.includes("expired")) {
              this.error = "This visitor's pass has expired.";
            }
            else if (message.includes("not scheduled")) {
              this.error = "This visitor is not scheduled to visit today.";
            }
            else {
              this.error = "QR verification failed.";
            }

            alert(this.error);
            break;
          }

          case 409:
            this.error = err.error?.message || err.error;
            if (!this.error) {
              this.error = "Visitor has already been processed.";
            }
            alert(this.error);
            break;

          default:

            this.error = "Unable to verify QR Code.";
            alert(this.error);

        }

        this.isScanning = true;
        this.startScanner();

      }
    });
  }

  stopScanner() {
    this.controls?.stop();
    this.router.navigate(['/guard-dashboard']);
  }

  ngOnDestroy(): void {
    this.controls?.stop();
  }

}
