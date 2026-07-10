import { Injectable } from '@angular/core';
import { VISITOR_URL } from '../globalUrl';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Visitor } from '../model/Visitor';
import { VisitorStatus } from '../model/VisitorStatus';
import { AuthService } from './auth-service';

@Injectable({
  providedIn: 'root',
})
export class VisitorService {
  private appUrl = `${VISITOR_URL}/visitor`;

  constructor(private http: HttpClient, private as: AuthService) { }

  private visitorSubject = new BehaviorSubject<Visitor[]>([]);
  visitors$ = this.visitorSubject.asObservable();

  addVisitor(visitor: Visitor): Observable<Visitor> {
    return this.http.post<Visitor>(this.appUrl, visitor)
      .pipe(tap(() => this.getAllVisitors()));
  }

  getAllVisitors(): void {
    this.http.get<Visitor[]>(this.appUrl)
      .subscribe(data => this.visitorSubject.next(data));
  }

  getVisitorById(visitorId: string): Observable<Visitor> {
    return this.http.get<Visitor>(`${this.appUrl}/${visitorId}`);
  }

  updateVisitor(visitorId: string, visitor: Visitor): Observable<Visitor> {
    return this.http.put<Visitor>(`${this.appUrl}/${visitorId}`, visitor)
      .pipe(tap(() => this.getAllVisitors()));
  }

  deleteVisitor(visitorId: string): Observable<string> {
    return this.http.delete(`${this.appUrl}/${visitorId}`, { responseType: 'text' })
      .pipe(tap(() => this.getAllVisitors()));
  }

  getMyVisitors(): Observable<Visitor[]> {
    return this.http.get<Visitor[]>(`${this.appUrl}/resident/my-visitors`);
  }

  getTodayVisitors(): Observable<Visitor[]> {
    return this.http.get<Visitor[]>(
      `${this.appUrl}/today`
    );
  }

  updateVisitorStatus(visitorId: string, status: VisitorStatus): Observable<Visitor> {
    return this.http.put<Visitor>(
      `${this.appUrl}/${visitorId}/${status}`, {})
      .pipe(tap(() => this.getAllVisitors()));
  }

  generatePass(visitorId: string): Observable<String> {
    return this.http.post(
      `${this.appUrl}/${visitorId}/generate-pass`, {}, { responseType: 'text' }
    );

  }

}
