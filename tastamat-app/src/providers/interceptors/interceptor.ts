import {TokenInterceptor} from "./token.interceptor";
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {NgModule} from "@angular/core";

@NgModule({
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true }
  ]
})
export class InterceptorModule { }
