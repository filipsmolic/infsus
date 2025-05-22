import { Routes } from '@angular/router';
import { NicotineIntakePageComponent } from './pages/nicotine-intake.page';
import { HomePageComponent } from './pages/home.page';
import { NicotineHistoryPageComponent } from './pages/nicotine-history.page';
import { LoginPageComponent } from './pages/login.page';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  { path: 'home', component: HomePageComponent },
  { path: 'nicotine-intake', component: NicotineIntakePageComponent },
  { path: 'nicotine-history', component: NicotineHistoryPageComponent },
];
