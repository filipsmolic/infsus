import { Routes } from '@angular/router';
import { NicotineIntakePageComponent } from './pages/nicotine-intake.page';
import { HomePageComponent } from './pages/home.page';

export const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'nicotine-intake', component: NicotineIntakePageComponent },
];
