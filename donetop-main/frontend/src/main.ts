import { bootstrapApplication } from "@angular/platform-browser";
import { provideRouter } from "@angular/router";
import { AppComponent } from "./app/app.component";
import { routes } from "./app/routes";
import { provideHttpClient } from "@angular/common/http";
import { importProvidersFrom } from "@angular/core";
import { StoreModule } from "@ngrx/store";
import { userReducer } from "./app/store/reducer/user.reducer";
import { CryptoService } from "./app/service/crypto.service";
import { environment } from "./environments/environment.development";

bootstrapApplication(
  AppComponent,
  {
    providers: [
      provideRouter(routes),
      provideHttpClient(),
      importProvidersFrom(StoreModule.forRoot({ user: userReducer })),
      { provide: CryptoService, useClass: environment.cryptoService }
    ]
  }
).catch(err => console.error(err));
