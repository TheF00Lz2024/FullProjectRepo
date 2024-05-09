import {Component, EventEmitter, Output} from '@angular/core';
import {
  FormControl,
  FormGroupDirective,
  NgForm,
  Validators,
  FormsModule,
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MusicAppAPIService } from '../Service/music-app-api.service';
import { user } from '../Modal/apiResponse';
import { HttpErrorResponse } from '@angular/common/http';

/** Error when invalid control is dirty, touched, or submitted. */
export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}
@Component({
  selector: 'app-sing-up',
  standalone: true,
  imports: [FormsModule, MatFormFieldModule, MatInputModule,  MatButtonModule, MatIconModule, ReactiveFormsModule, CommonModule, MatProgressSpinnerModule],
  templateUrl: './sing-up.component.html',
  styleUrl: './sing-up.component.css'
})
export class SingUpComponent {
  //pass an event to the parent (login component)
  @Output() userSingUp = new EventEmitter<boolean>();
  //form angular material form (email) to validate user input
  matcher = new MyErrorStateMatcher();
  //for the icon button to hide or show password
  hidePassword = true;
  //for the icon button to hid or show confirm password
  hideConfirmPassword = true;
  //for the animation for the spinner to show the page is processing the request
  userloading: boolean = false;

  //for creating a reactive form
  userSignUpForm: FormGroup;

  get usernameControl(): FormControl{
    return this.userSignUpForm.get('username') as FormControl;
  }

  get passwordControl(): FormControl{
    return this.userSignUpForm.get('password') as FormControl;
  }

  get confirmPasswordControl(): FormControl{
    return this.userSignUpForm.get('confirmPassword') as FormControl;
  }

  //creating a constructio to build the form, and calling API Service for the creating of user account
  constructor(private formBuilder: FormBuilder, private loginAPIService: MusicAppAPIService){
    this.userSignUpForm = this.formBuilder.group({
      username: this.formBuilder.control('', [Validators.required, Validators.email]),
      //simple password validation where only need 1upper and lower case and 1 number min 8 character
      password: this.formBuilder.control('', [Validators.required, Validators.pattern(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/)]),
      //have the same regex pattern as password
      confirmPassword: this.formBuilder.control('', [Validators.required, Validators.pattern(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/)])
    })
  }
  // to go back to login page (reset the form value if there is any input)
  login(){
    this.usernameControl.setValue('');
    this.passwordControl.setValue('');
    this.confirmPasswordControl.setValue('');
    this.userSingUp.emit(false);
  }

  // to create an account (once the button to create account is being click)
  userSignUp(){
    // set loading visible
    this.userloading = true;

    // create user obj
    const newUser = {
      username: `${this.usernameControl.value}`,
      password: `${this.passwordControl.value}`
    } as user;

    //pass the user obj to API service to create the user account
    this.loginAPIService.createAccount(newUser).subscribe({
      next:(data => {
        //if there is no issue add some form of delay to show the website is processing the request
        setTimeout(() => {
          //reset the sing up page form value 
          this.userloading = true;
          this.usernameControl.setValue('');
          this.passwordControl.setValue('');
          this.confirmPasswordControl.setValue('');
          //sent an event to the parent (login component) to bring the user back to login page (automatically)
          this.userSingUp.emit(false);
          //notify the user that their account ahd been created and can be log in
          alert(`Account has been created! \nUsername: ${data.username}!\nBack to Login Page!`);
        }, 3000)
      }), error: ((err: HttpErrorResponse) => {
        //will shwo this error if this email address is already in use
        if(err.status === 403){
          alert('This Username already have been used!');
          console.log(`Duplicate username: ${err.message}`)
        } 
        // will show this error if something went wrong
        else{
          alert('Error has occur!');
          console.log(`Error had occur: ${err}`);
        }
        this.userloading = false;
      })
    })
  }
}
