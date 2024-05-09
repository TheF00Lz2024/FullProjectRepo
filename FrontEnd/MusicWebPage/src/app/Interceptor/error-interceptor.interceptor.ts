import { HttpHandlerFn, HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

//global interceptor class to intercept any form of error if have and throw it to component to catch it and resolve the issue
export const errorInterceptorInterceptor: HttpInterceptorFn = (req, next: HttpHandlerFn) => {
  return next(req).pipe(catchError((error) => {
    console.log(`Cuaght in Error Interceptor: ${error.message}`)
    return throwError(() => error);
  }));
};
