import {Component} from '@angular/core';
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
import { ErrorStateMatcher } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { SingUpComponent } from '../sing-up/sing-up.component';
import { Router, RouterOutlet } from '@angular/router';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { MusicAppAPIService } from '../Service/music-app-api.service';
import { authorization } from '../Modal/apiResponse';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { HttpErrorResponse } from '@angular/common/http';
import { SessionServiceService } from '../Service/session-service.service';
import { WelcomePageComponent } from '../welcome-page/welcome-page.component';

/** Error when invalid control is dirty, touched, or submitted. */
export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, MatFormFieldModule, MatInputModule,  MatButtonModule, MatIconModule, 
    ReactiveFormsModule, CommonModule, SingUpComponent, RouterOutlet, NgbDropdownModule, MatProgressSpinnerModule, WelcomePageComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  matcher = new MyErrorStateMatcher();
  // for the icon press to see and hide password
  hidePassword = true;
  // for the icon press to see and hise COnfirm Password
  hideConfirmPassword = true;
  userSingUp: boolean = false;
  userLogIn: boolean = false;
  // flag for the loading animation
  userloading: boolean = false;
  loginStatus: authorization[] = [];

  // create form group via reactive form
  userSignUpForm: FormGroup;

  get usernameControl(): FormControl{
    return this.userSignUpForm.get('username') as FormControl;
  }

  get passwordControl(): FormControl{
    return this.userSignUpForm.get('password') as FormControl;
  }

  //create constructor to build form, use custom services
  constructor(private formBuilder: FormBuilder, private router: Router, private loginAPIService: MusicAppAPIService, private sessionStorage: SessionServiceService){
    this.userSignUpForm = this.formBuilder.group({
      username: this.formBuilder.control('', [Validators.required, Validators.email]),
      password: this.formBuilder.control('', [Validators.required]),
    })
  }

  // button function when user press sign up
  singUp(){
    this.usernameControl.setValue('');
    this.passwordControl.setValue('');
    this.userSingUp = true;
  }

  // button function when user press log in
  loginToWebApp(){
    this.userloading = true
    // call API service and comms to login web server and authenticate user
    this.loginAPIService.getUserLogin(this.usernameControl.value, this.passwordControl.value)
    .subscribe({
      next:(data) => {
        // if there is user comms to music web server and check JWT token
        this.loginAPIService.authorizeUserLogin(data.token)
        .subscribe((confirmation) => {
          //if no issue allow user to continue using the app
          if(confirmation.authorizationMessage === 'You have login successfully'){
            setTimeout(() => {
              this.userLogIn = true;
              this.userloading = false;
              // call session storgae service
              this.sessionStorage.setUsernameSession(`${this.usernameControl.value}`);
              this.sessionStorage.setAuthenticationSession(data.token);
            }, 3000);
          }
        });
      },
      // this error will happen when there is no match of username and password 
      error: (err: HttpErrorResponse) => {
        if(err.status === 404){
          alert('Invalid Username or Password!');
          console.log(`Username and password mismatched`);
        }else{
          alert('Error has occur!');
          console.log(`Error had occur: ${err}`);
        }
        this.userloading = false;
      }
    });
  }

  // function for logout and reset whatever is stored to empty
  useerLogOut(){
    this.userloading = true
    setTimeout(() => {
      this.userLogIn = false;
      this.userloading = false;
      // reset the form value
      this.usernameControl.setValue('');
      this.passwordControl.setValue('');
      // reset session value
      this.sessionStorage.setUsernameSession('');
      this.sessionStorage.setAuthenticationSession('');
      alert('You have logout from the application.');
    }, 3000);


  }

  //Get Data from child (child is sign-up component)
  backToLoginPage(data: boolean){
    this.userSingUp = data;
  }

  //get sessionStorageData (create function and call session service to get username)
  getSessionStorageDataUsername(){
    return this.sessionStorage.getUsernameSession();
  }
}


