import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

import { PASS_URL } from '../globalUrl';
import { Pass } from '../model/Pass';

@Injectable({
  providedIn: 'root',
})
export class PassService {

  private appUrl = `${PASS_URL}/pass`;

  constructor(private http: HttpClient) { }

  private passSubject = new BehaviorSubject<Pass[]>([]);
  passes$ = this.passSubject.asObservable();

  generatePass(visitorId: string): Observable<Pass> {
    return this.http.post<Pass>(
      `${this.appUrl}/generate/${visitorId}`,
      {}
    ).pipe(tap(() => this.getAllPasses()));
  }

  getAllPasses(): void {
    this.http.get<Pass[]>(this.appUrl)
      .subscribe(data => this.passSubject.next(data));
  }

  getPassById(passId: string): Observable<Pass> {
    return this.http.get<Pass>(`${this.appUrl}/${passId}`);
  }

  getPassForVisitor(visitorId: string): Observable<Pass> {
    return this.http.get<Pass>(
      `${this.appUrl}/visitor/${visitorId}`
    );
  }

  verifyPass(qrCode: string): Observable<Pass> {
    return this.http.get<Pass>(
      `${this.appUrl}/verify/${qrCode}`
    );
  }

  markPassUsed(passId: string): Observable<Pass> {
    return this.http.put<Pass>(
      `${this.appUrl}/use/${passId}`,
      {}
    ).pipe(tap(() => this.getAllPasses()));
  }

  cancelPass(passId: string): Observable<Pass> {
    return this.http.put<Pass>(
      `${this.appUrl}/cancel/${passId}`,
      {}
    ).pipe(tap(() => this.getAllPasses()));
  }

  deletePass(passId: string): Observable<string> {
    return this.http.delete(`${this.appUrl}/${passId}`, {
      responseType: 'text'
    }).pipe(tap(() => this.getAllPasses()));
  }

}