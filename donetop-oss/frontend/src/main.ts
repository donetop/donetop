import { bootstrapApplication } from "@angular/platform-browser";
import { provideRouter } from "@angular/router";
import { routes } from "./app/routes";
import { importProvidersFrom } from "@angular/core";
import { StoreModule } from "@ngrx/store";
import { ossUserReducer } from "./app/store/reducer/oss-user.reducer";
import { AppComponent } from "./app/app.component";
import { provideHttpClient } from "@angular/common/http";

bootstrapApplication(
  AppComponent,
  {
    providers: [
      provideRouter(routes),
      provideHttpClient(),
      importProvidersFrom(StoreModule.forRoot({ ossUser: ossUserReducer }))
    ]
  }
).catch(err => console.error(err));
