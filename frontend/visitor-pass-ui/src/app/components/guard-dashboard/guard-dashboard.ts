import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Visitor } from '../../model/Visitor';
import { VisitorStatus } from '../../model/VisitorStatus';
import { VisitorService } from '../../services/visitor-service';
import { PassService } from '../../services/pass-service';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-guard-dashboard',
  imports: [RouterLink],
  templateUrl: './guard-dashboard.html',
  styleUrl: './guard-dashboard.css',
})
export class GuardDashboard implements OnInit {
  constructor(
    private visitorService: VisitorService,
    private passService: PassService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) { }

  visitors: Visitor[] = [];
  isLoading = true;
  verifiedVisitorId = '';

  searchText = '';
  selectedStatus = 'ALL';

  filteredVisitors: Visitor[] = [];

  totalVisitors = 0;
  pendingCount = 0;
  approvedCount = 0;
  enteredCount = 0;
  exitedCount = 0;
  cancelledCount = 0;
  expiredCount = 0;

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.verifiedVisitorId = params['verifiedVisitor'] || '';
      this.loadTodayVisitors();
    });
  }

  loadTodayVisitors() {
    this.isLoading = true;
    this.visitorService.getTodayVisitors().subscribe({
      next: (data) => {
        this.visitors = data;
        this.calculateDashboardCards();
        this.applyFilters();
        if (this.verifiedVisitorId) {

          const visitor = this.visitors.find(
            v => v.id === this.verifiedVisitorId
          );
          if (visitor) {
            alert(visitor.visitorName + " verified successfully.");
          }
          else {
            alert("Scanned QR does not belong to today's visitors.");
            this.verifiedVisitorId = '';
          }
        }
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.isLoading = false;
      }
    });
  }



  performAction(visitor: Visitor, action: string) {

    switch (action) {

      case 'APPROVED':
        this.approveVisitor(visitor);
        break;

      case 'CANCELLED':
        this.cancelVisitor(visitor);
        break;

      case 'ENTERED':
        this.markEntered(visitor);
        break;

      case 'EXITED':
        this.markExited(visitor);
        break;

    }

  }

  approveVisitor(visitor: Visitor) {

    this.visitorService.updateVisitorStatus(
      visitor.id!,
      VisitorStatus.APPROVED
    ).subscribe({
      next: (updatedVisitor) => {
        visitor.status = updatedVisitor.status;
        this.verifiedVisitorId = '';
        this.loadTodayVisitors();
        alert("Visitor Approved");

      },
      error: (err) => {
        console.error(err);
        alert("Unable to approve visitor.");
      }
    });
  }

  cancelVisitor(visitor: Visitor) {
    this.visitorService.updateVisitorStatus(
      visitor.id!,
      VisitorStatus.CANCELLED)
      .subscribe({
        next: (updatedVisitor) => {
          visitor.status = updatedVisitor.status;
          this.verifiedVisitorId = '';
          this.passService.getPassForVisitor(visitor.id!).subscribe({
            next: (pass) => {
              this.passService.cancelPass(pass.passId!).subscribe({
                next: () => {
                  this.loadTodayVisitors();
                }
              });
            },
            error: () => this.loadTodayVisitors()
          });
          alert("Visitor Cancelled");
        },
        error: (err) => {
          console.error(err);
        }
      });
  }

  markEntered(visitor: Visitor) {
    this.visitorService.updateVisitorStatus(
      visitor.id!,
      VisitorStatus.ENTERED)
      .subscribe({
        next: (updatedVisitor) => {
          visitor.status = updatedVisitor.status;
          this.verifiedVisitorId = '';
          this.passService.getPassForVisitor(visitor.id!).subscribe({
            next: (pass) => {
              this.passService.markPassUsed(pass.passId!).subscribe({
                next: () => {
                  alert("Visitor Entry Recorded");
                  this.loadTodayVisitors();
                },
                error: (err) => {
                  console.error(err);
                }
              });
            }
          });
        },
        error: (err) => {
          console.error(err);
        }
      });
  }

  markExited(visitor: Visitor) {
    this.visitorService.updateVisitorStatus(
      visitor.id!,
      VisitorStatus.EXITED
    ).subscribe({
      next: (updatedVisitor) => {
        visitor.status = updatedVisitor.status;
        this.verifiedVisitorId = '';
        alert("Visitor Exited");
        this.loadTodayVisitors();
      },

      error: (err) => {
        console.error(err);
        alert("Unable to update visitor.");
      }
    });
  }

  calculateDashboardCards() {

    this.totalVisitors = this.visitors.length;

    this.pendingCount =
      this.visitors.filter(v => v.status === 'PENDING').length;

    this.approvedCount =
      this.visitors.filter(v => v.status === 'APPROVED').length;

    this.enteredCount =
      this.visitors.filter(v => v.status === 'ENTERED').length;

    this.exitedCount =
      this.visitors.filter(v => v.status === 'EXITED').length;

    this.cancelledCount =
      this.visitors.filter(v => v.status === 'CANCELLED').length;

    this.expiredCount =
      this.visitors.filter(v => v.status === 'EXPIRED').length;

  }

  applyFilters() {

    this.filteredVisitors = this.visitors.filter(visitor => {

      const matchesSearch =
        visitor.visitorName.toLowerCase()
          .includes(this.searchText.toLowerCase()) ||

        visitor.visitorPhone.includes(this.searchText);

      const matchesStatus =
        this.selectedStatus === 'ALL' ||
        visitor.status === this.selectedStatus;

      return matchesSearch && matchesStatus;

    });

  }

  onSearch(event: Event) {

    this.searchText =
      (event.target as HTMLInputElement).value;

    this.applyFilters();

  }

  onStatusChange(event: Event) {

    this.selectedStatus =
      (event.target as HTMLSelectElement).value;

    this.applyFilters();

  }

}
