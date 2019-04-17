import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import { _throw } from 'rxjs/observable/throw';
import { catchError } from 'rxjs/operators';
import { Storage } from '@ionic/storage';
import { LoaderProvider } from "../loader.provider";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private storage: Storage, private loader: LoaderProvider) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.loader.show();
    if (request.url.includes("/platform/v1/rest/a/")) {
      let promise = this.storage.get('token');

      return Observable.fromPromise(promise)
        .mergeMap(token => {
          let clonedReq = this.addToken(request, token);
          return next.handle(clonedReq)
            .pipe(catchError(error => {
              return _throw(error);
            }))
            .finally(() => this.loader.hide());
        });
    } else {
      return next.handle(request).finally(() => this.loader.hide());
    }
  }

  private addToken(request: HttpRequest<any>, token: any) {
    if (token) {
      let clone: HttpRequest<any>;
      clone = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      return clone;
    }
    return request;
  }
}
