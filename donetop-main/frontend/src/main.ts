import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AppModule } from './app/app.module';
import { environment } from './environments/environment.development';

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => {
    if (!environment.production) {
      console.error(err);
    }
  });
