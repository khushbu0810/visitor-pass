import { Component, OnInit } from '@angular/core';

import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ResidentService } from '../../services/resident-service';

@Component({
  selector: 'app-edit-resident',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './edit-resident.html',
  styleUrl: './edit-resident.css',
})
export class EditResident implements OnInit {

  residentForm!: FormGroup;
  residentId!: string;

  constructor(
    private fb: FormBuilder,
    private rs: ResidentService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.residentForm = this.fb.group({
      fullName: [''],
      email: [''],
      phoneNumber: [''],
      flatNumber: [''],
      tower: [''],
    });

    this.residentId = this.route.snapshot.paramMap.get('id') || '';
    if (this.residentId) {
      this.loadResident();
    }
  }

  loadResident() {
    this.rs.getResidentById(this.residentId).subscribe(resident => {
      this.residentForm.patchValue(resident);
    });
  }

  updateResident() {
    if (this.residentForm.invalid) return;
    const updatedResident = this.residentForm.getRawValue();
    this.rs.updateResident(this.residentId, updatedResident).subscribe(() => {
      alert('Resident updated successfully');
      this.router.navigate(['/resident-profile']);
    });
  }

  get f() {
    return this.residentForm.controls;
  }
}
