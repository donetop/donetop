import { Injectable } from '@angular/core';
import { NavigationCancel, NavigationEnd, NavigationError, NavigationStart, RouterEvent } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class LoaderService {

  private _loading = false;

  acceptRouterEvent(e: RouterEvent) {
    if (e instanceof NavigationStart) {
      this.loading = true;
    }
    if (e instanceof NavigationEnd) {
      this.loading = false;
    }
    if (e instanceof NavigationCancel) {
      this.loading = false;
    }
    if (e instanceof NavigationError) {
      this.loading = false;
    }
  }

  get loading() {
    return this._loading;
  }

  set loading(loading: boolean) {
    this._loading = loading;
  }
}
