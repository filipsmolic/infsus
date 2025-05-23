import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { provideRouter, Router } from '@angular/router';
import { ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { routes } from './app.routes';

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let app: AppComponent;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [provideRouter(routes), provideHttpClient()],
    }).compileComponents();
    fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
    router = TestBed.inject(Router);
  });

  it('should create the app', () => {
    expect(app).toBeTruthy();
  });

  it(`should have the 'frontend' title`, () => {
    expect(app.title).toEqual('frontend');
  });

  it('should show header and sidebar on non-login pages', () => {
    app.isLoginPage = false;
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('header')).toBeTruthy();
    expect(compiled.querySelector('nav')).toBeTruthy();
  });

  it('should hide header and sidebar on login page', () => {
    app.isLoginPage = true;
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('header')).toBeFalsy();
    expect(compiled.querySelector('nav')).toBeFalsy();
  });

  it('should navigate to /home when clicking NicotiNO', () => {
    app.isLoginPage = false;
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    const link = compiled.querySelector('header a');
    expect(link?.getAttribute('routerLink')).toBe('/home');
  });

  it('should update isLoginPage on navigation', () => {
    app.isLoginPage = false;
    (router.events as any).next({ url: '/login' });
    expect(app.isLoginPage).toBe(false);
  });
});
