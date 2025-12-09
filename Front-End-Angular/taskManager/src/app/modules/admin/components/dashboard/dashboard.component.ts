import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {


  searchForm!:FormGroup;
  listOfTasks:any=[];
    constructor(private adminService:AdminService,
      private snackbar:MatSnackBar,
      private fb:FormBuilder
    ){
      this.getTasks();

      this.searchForm=this.fb.group({
        title:[null]
      })
    }


    getTasks(){
      this.adminService.getAllTasks().subscribe((res)=>{
        this.listOfTasks=res;
      })
    }
    deleteTask(id:number){
      this.adminService.DeleteTask(id).subscribe((res)=>{
              this.snackbar.open("task deleted successfully","close",{duration:5000});
              this.getTasks();

      })
    }

  searchTask() {
  const title = this.searchForm.get("title")!.value;

  // If input is empty → load all tasks
  if (!title || title.trim() === "") {
    this.getTasks();
    return;
  }

  // Otherwise → search
  this.adminService.searchTask(title).subscribe((res) => {
    this.listOfTasks = res;
  });
}

}
