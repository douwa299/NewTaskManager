import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StorageService } from '../../../auth/services/storage/storage.service';
import { Observable } from 'rxjs/internal/Observable';
  const BASE_URL = 'http://localhost:9090/';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private http:HttpClient,
    private storageService:StorageService
  ) { }
   private createAuthorizationHeader(): HttpHeaders {
    const token = this.storageService.getToken();
    console.log("Voici le token"+token);
    // HttpHeaders is immutable, so you need to create new ones properly
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + token)
      .set('Content-Type', 'application/json');
  }



      getEmployeeTasksById(id: number): Observable<any> {
  const headers = this.createAuthorizationHeader();

  console.log("=== TOKEN SENT BY ANGULAR ===", headers.get("Authorization"));

  return this.http.get(
    BASE_URL + "api/employee/tasks/" + id,
    { headers }
  );
}
updateStatus(id: number, status: string): Observable<any> {
  const headers = this.createAuthorizationHeader();

  console.log("=== TOKEN SENT BY ANGULAR ===", headers.get("Authorization"));

  return this.http.put(
    BASE_URL + "api/employee/task/" + id + "/" + status,
    {}, // empty body
    { headers }
  );
}


}
