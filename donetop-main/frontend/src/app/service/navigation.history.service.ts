import { Injectable } from "@angular/core";
import { NavigationEnd, Router } from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class NavigationHistoryService {

  private _histories: Array<string> = [];

  constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this._histories.push(event.urlAfterRedirects);
      }
    });
  }

  get histories() {
    return this._histories;
  }

  get previousUrl() {
    if (this._histories.length > 0) return this._histories[this._histories.length - 2];
    return '';
  }

}
