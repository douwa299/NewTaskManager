import { Component } from '@angular/core';
import { EmployeeService } from '../../services/employee.service';
import { StorageService } from '../../../../auth/services/storage/storage.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {


  listOfTasks:any=[];
  constructor(private service : EmployeeService,
    private storage:StorageService,
    private snackbar:MatSnackBar
  ){
    this.getTasks();
  }

  getTasks(){
    this.service.getEmployeeTasksById(this.storage.getUser()?.id).subscribe((res)=>{
      this.listOfTasks=res;
      console.log(res);
    })
  }

  updateStatus(id:number,status:string){
    this.service.updateStatus(id,status).subscribe((res)=>{
            if(res!==null)
              {
                    this.snackbar.open("task status updated successfully","close",{duration:5000});
                              this.getTasks();
              }
     })

  }

}
