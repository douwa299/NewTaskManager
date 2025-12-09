import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-update-task',
  standalone: false,
  templateUrl: './update-task.component.html',
  styleUrls: ['./update-task.component.scss']  // FIXED
})
export class UpdateTaskComponent {

  id!: number;

  updatetaskForm!:FormGroup;
  listOfEmployees:any=[];
  listOfPriorities:any=["LOW","MEDIUM","HIGH"];
  listOfStatus:any=["INPROGRESS","CANCELLED","COMPLETED","DEFERRED","PENDING"];
  constructor(
    private service: AdminService,
    private route: ActivatedRoute,
    private fb:FormBuilder,
    private snackbar:MatSnackBar,
    private router:Router
  ) {
    this.id = this.route.snapshot.params['id']; // FIXED
    this.getTaskById();
    this.getUsers();
    this.updatetaskForm=this.fb.group({
        employeeId:[null,[Validators.required]],
        title:[null,[Validators.required]],
        description:[null,[Validators.required]],
        dueDate:[null,[Validators.required]],
        priority:[null,[Validators.required]],
        taskStatus:[null,[Validators.required]],


    })
  }

  getTaskById() {
    this.service.getTaskById(this.id).subscribe((res) => {
      this.updatetaskForm.patchValue(res);
      console.log('TASK FOUND:', res);
    });
  }
  
   getUsers(){
    this.service.getUsers().subscribe((res)=>{
      this.listOfEmployees=res;
      console.log(res);
    })
  }
  updateTask() {
  const formValue = this.updatetaskForm.value;

  // Format the dueDate if it's a Date object
  if (formValue.dueDate instanceof Date) {
    formValue.dueDate = this.formatDate(formValue.dueDate);
  }

  this.service.updateTask(this.id, formValue).subscribe({
    next: (res) => {
      if (res.id !== null) {
        this.snackbar.open("Task updated successfully", "Close", { duration: 5000 });
        this.router.navigateByUrl("/admin/dashboard");
      } else {
        this.snackbar.open("Task was not updated", "Error", { duration: 5000 });
      }
    },
    error: (error) => {
      console.error('Update task error:', error);
      this.snackbar.open("Failed to update task: " + error.message, "Error", { duration: 5000 });
    }
  });
}

private formatDate(date: Date): string {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  return `${year}-${month}-${day}`;
}
}
