import { Injectable } from '@angular/core';
import { Resident } from '../model/Resident';
import { tap } from 'rxjs/internal/operators/tap';
import { Observable } from 'rxjs/internal/Observable';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { HttpClient } from '@angular/common/http';
import { RESIDENT_URL } from '../globalUrl';

@Injectable({
  providedIn: 'root',
})
export class ResidentService {
  private appUrl = `${RESIDENT_URL}/residents`;

  constructor(private http: HttpClient) { }

  private residentSubject = new BehaviorSubject<Resident[]>([]);
  residents$ = this.residentSubject.asObservable();

  // addResident(resident: Resident): Observable<Resident> {
  //   return this.http.post<Resident>(this.appUrl, resident);
  // }

  getAllResidents(): void {
    this.http.get<Resident[]>(this.appUrl)
      .subscribe(data => this.residentSubject.next(data));
  }

  getResidentById(id: string): Observable<Resident> {
    return this.http.get<Resident>(`${this.appUrl}/${id}`)
  }

  updateResident(id: string, resident: Resident): Observable<Resident> {
    return this.http.put<Resident>(`${this.appUrl}/${id}`, resident);
  }

  deleteResident(id: string): Observable<String> {
    return this.http.delete(`${this.appUrl}/${id}`, { responseType: 'text' })
      .pipe(tap(() => this.getAllResidents()));
  }

  getResidentProfile(userId: number) {
    return this.http.get<any>(`${this.appUrl}/user/${userId}`);
  }

}
