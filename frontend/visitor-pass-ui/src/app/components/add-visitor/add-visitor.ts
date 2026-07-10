import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { VisitorService } from '../../services/visitor-service';


@Component({
  selector: 'app-add-visitor',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './add-visitor.html',
  styleUrl: './add-visitor.css'
})
export class AddVisitor implements OnInit {

  visitorForm!: FormGroup;
  isSubmitting = false;

  constructor(
    private fb: FormBuilder,
    private visitorService: VisitorService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.visitorForm = this.fb.group({
      visitorName: [''],
      visitorPhone: [''],
      visitorEmail: [' '],
      purpose: [''],
      visitDate: [''],
      status: [{ value: 'PENDING', disabled: true }]
    });
  }

  addVisitor() {
    if (this.visitorForm.invalid || this.isSubmitting) {
      return;
    }

    this.isSubmitting = true;

    this.visitorService.addVisitor(this.visitorForm.getRawValue()).subscribe({
      next: (visitor) => {
        console.log('Visitor added:', visitor);
        this.isSubmitting = false;
        alert("Visitor added successfully. Redirecting you to the resident profile page.");
        this.router.navigate(['/resident-profile']);
      },
      error: (err) => {
        console.error(err);
        this.isSubmitting = false;
        alert("Failed to add visitor.");
      }
    });
  }

  get f() {
    return this.visitorForm.controls;
  }

}