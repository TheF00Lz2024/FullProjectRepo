import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SessionServiceService {

  constructor() { }

  // set sessionStorage for username
  setUsernameSession(username: string){
    sessionStorage.setItem('username', username);
  }

  // set sessionStorage for web server auth token
  setAuthenticationSession(token: string){
    sessionStorage.setItem('token', token);
  }

  //get sessionStorage for username
  getUsernameSession(){
    return sessionStorage.getItem('username');
  }

    //get sessionStorage for auth token
    getAuthenticationSession(){
      return sessionStorage.getItem('token');
    }
}
