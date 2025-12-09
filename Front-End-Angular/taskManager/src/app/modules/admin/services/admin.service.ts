  import { HttpClient, HttpHeaders, HttpErrorResponse } from "@angular/common/http";
  import { Injectable } from "@angular/core";
  import { Observable, catchError, tap, throwError } from "rxjs";
  import { StorageService } from "../../../auth/services/storage/storage.service";

  const BASE_URL = 'http://localhost:9090/';

  @Injectable({
    providedIn: 'root'
  })
  export class AdminService {

    constructor(
      private http: HttpClient,
      private storageService: StorageService
    ) {}

    getUsers(): Observable<any> {
      const token = this.storageService.getToken();
      const headers = this.createAuthorizationHeader();

      console.log('=== DEBUG: Admin Service ===');
      console.log('Full URL:', BASE_URL + 'api/admin/users');
      console.log('Token from localStorage:', token);
      console.log('Headers being sent:', headers);

      return this.http.get(BASE_URL + "api/admin/users", { headers })
        .pipe(
          tap(response => {
            console.log('=== SUCCESS Response ===', response);
          }),
          catchError((error: HttpErrorResponse) => {
            console.log('=== ERROR Details ===');
            console.log('Error Status:', error.status);
            console.log('Error Status Text:', error.statusText);
            console.log('Error Message:', error.message);
            console.log('Error Name:', error.name);
            console.log('Error URL:', error.url);
            console.log('Full Error Object:', error);

            if (error.error instanceof ErrorEvent) {
              console.log('Client-side error:', error.error.message);
            } else {
              console.log('Server-side error:', error.status, error.error);
            }

            return throwError(() => error);
          })
        );
    }

    postTask(taskDTO:any): Observable<any> {
      const token = this.storageService.getToken();
      const headers = this.createAuthorizationHeader();

      console.log('=== DEBUG: Admin Service ===');
      console.log('Full URL:', BASE_URL + 'api/admin/users');
      console.log('Token from localStorage:', token);
      console.log('Headers being sent:', headers);

      return this.http.post(BASE_URL + "api/admin/task",taskDTO, { headers })
        .pipe(
          tap(response => {
            console.log('=== SUCCESS Response ===', response);
          }),
          catchError((error: HttpErrorResponse) => {
            console.log('=== ERROR Details ===');
            console.log('Error Status:', error.status);
            console.log('Error Status Text:', error.statusText);
            console.log('Error Message:', error.message);
            console.log('Error Name:', error.name);
            console.log('Error URL:', error.url);
            console.log('Full Error Object:', error);

            if (error.error instanceof ErrorEvent) {
              console.log('Client-side error:', error.error.message);
            } else {
              console.log('Server-side error:', error.status, error.error);
            }

            return throwError(() => error);
          })
        );
    }
    getAllTasks(): Observable<any> {
      const token = this.storageService.getToken();
      const headers = this.createAuthorizationHeader();

      console.log('=== DEBUG: Admin Service ===');
      console.log('Full URL:', BASE_URL + 'api/admin/tasks');
      console.log('Token from localStorage:', token);
      console.log('Headers being sent:', headers);

      return this.http.get(BASE_URL + "api/admin/tasks", { headers })
        .pipe(
          tap(response => {
            console.log('=== SUCCESS Response ===', response);
          }),
          catchError((error: HttpErrorResponse) => {
            console.log('=== ERROR Details ===');
            console.log('Error Status:', error.status);
            console.log('Error Status Text:', error.statusText);
            console.log('Error Message:', error.message);
            console.log('Error Name:', error.name);
            console.log('Error URL:', error.url);
            console.log('Full Error Object:', error);

            if (error.error instanceof ErrorEvent) {
              console.log('Client-side error:', error.error.message);
            } else {
              console.log('Server-side error:', error.status, error.error);
            }

            return throwError(() => error);
          })
        );
    }

    DeleteTask(id:number): Observable<any> {
      const token = this.storageService.getToken();
      const headers = this.createAuthorizationHeader();

      console.log('=== DEBUG: Admin Service ===');
      console.log('Full URL:', BASE_URL + 'api/admin/task');
      console.log('Token from localStorage:', token);
      console.log('Headers being sent:', headers);

      return this.http.delete(BASE_URL + "api/admin/task/" + id, { headers })
        .pipe(
          tap(response => {
            console.log('=== SUCCESS Response ===', response);
          }),
          catchError((error: HttpErrorResponse) => {
            console.log('=== ERROR Details ===');
            console.log('Error Status:', error.status);
            console.log('Error Status Text:', error.statusText);
            console.log('Error Message:', error.message);
            console.log('Error Name:', error.name);
            console.log('Error URL:', error.url);
            console.log('Full Error Object:', error);

            if (error.error instanceof ErrorEvent) {
              console.log('Client-side error:', error.error.message);
            } else {
              console.log('Server-side error:', error.status, error.error);
            }

            return throwError(() => error);
          })
        );
    }
    getTaskById(id:number): Observable<any> {
      const token = this.storageService.getToken();
      const headers = this.createAuthorizationHeader();

      console.log('=== DEBUG: Admin Service ===');
      console.log('Full URL:', BASE_URL + 'api/admin/task');
      console.log('Token from localStorage:', token);
      console.log('Headers being sent:', headers);

      return this.http.get(BASE_URL + "api/admin/task/" + id, { headers })
        .pipe(
          tap(response => {
            console.log('=== SUCCESS Response ===', response);
          }),
          catchError((error: HttpErrorResponse) => {
            console.log('=== ERROR Details ===');
            console.log('Error Status:', error.status);
            console.log('Error Status Text:', error.statusText);
            console.log('Error Message:', error.message);
            console.log('Error Name:', error.name);
            console.log('Error URL:', error.url);
            console.log('Full Error Object:', error);

            if (error.error instanceof ErrorEvent) {
              console.log('Client-side error:', error.error.message);
            } else {
              console.log('Server-side error:', error.status, error.error);
            }

            return throwError(() => error);
          })
        );
    }


    updateTask(id:number, taskDTO:any): Observable<any> {
        const headers = this.createAuthorizationHeader();

        console.log("=== TOKEN SENT BY ANGULAR ===", headers.get("Authorization"));

        return this.http.put(
          BASE_URL + "api/admin/task/" + id,
          taskDTO,
          { headers }
        );
      }

      searchTask(title:String): Observable<any> {
        const headers = this.createAuthorizationHeader();

        console.log("=== TOKEN SENT BY ANGULAR ===", headers.get("Authorization"));

        return this.http.get(
          BASE_URL + "api/admin/task/search/" + title,
          { headers }
        );
      }

    private createAuthorizationHeader(): HttpHeaders {
    const token = this.storageService.getToken();
    console.log("Voici le token"+token);
    // HttpHeaders is immutable, so you need to create new ones properly
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + token)
      .set('Content-Type', 'application/json');
  }
  }
