import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="min-h-screen flex items-center justify-center bg-gray-900">
      <div class="w-full max-w-md bg-gray-800 rounded-2xl shadow-lg p-10">
        <div class="text-center mb-6">
          <div class="text-4xl font-bold text-[#73EC8B] mb-2 drop-shadow">
            NicotiNO
          </div>
          <h2 class="text-2xl font-bold text-[#73EC8B]">Login</h2>
        </div>
        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()" class="space-y-6">
          <div>
            <label for="email" class="block mb-1 text-gray-300 font-medium"
              >Email</label
            >
            <input
              type="email"
              id="email"
              formControlName="email"
              placeholder="Unesite email"
              class="w-full p-3 rounded bg-gray-700 text-white focus:outline-none focus:ring-2 focus:ring-[#73EC8B]"
            />
            <div
              class="text-red-400 text-sm mt-1"
              *ngIf="
                loginForm.get('email')?.invalid &&
                (loginForm.get('email')?.dirty ||
                  loginForm.get('email')?.touched)
              "
            >
              <span *ngIf="loginForm.get('email')?.errors?.['required']"
                >Email je obavezan.</span
              >
              <span *ngIf="loginForm.get('email')?.errors?.['pattern']"
                >Unesite ispravnu email adresu.</span
              >
            </div>
          </div>
          <div>
            <label for="password" class="block mb-1 text-gray-300 font-medium"
              >Lozinka</label
            >
            <input
              type="password"
              id="password"
              formControlName="password"
              placeholder="Unesite lozinku"
              class="w-full p-3 rounded bg-gray-700 text-white focus:outline-none focus:ring-2 focus:ring-[#73EC8B]"
            />
            <div
              class="text-red-400 text-sm mt-1"
              *ngIf="
                loginForm.get('password')?.invalid &&
                (loginForm.get('password')?.dirty ||
                  loginForm.get('password')?.touched)
              "
            >
              <span *ngIf="loginForm.get('password')?.errors?.['required']"
                >Lozinka je obavezna.</span
              >
              <span *ngIf="loginForm.get('password')?.errors?.['minlength']"
                >Lozinka mora imati barem 8 znakova.</span
              >
              <span *ngIf="loginForm.get('password')?.errors?.['pattern']"
                >Lozinka mora sadržavati veliko i malo slovo, broj i poseban
                znak.</span
              >
            </div>
          </div>
          <button
            type="submit"
            [disabled]="loginForm.invalid"
            class="w-full py-3 rounded bg-[#73EC8B] hover:bg-[#5dbd72] text-gray-900 font-semibold transition disabled:bg-gray-600 disabled:text-gray-400 disabled:cursor-not-allowed"
          >
            Prijava
          </button>
        </form>
      </div>
    </div>
  `,
  styles: [],
})
export class LoginPageComponent {
  loginForm: FormGroup;

    
  private emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    
    
  private passwordPattern =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

  constructor(private fb: FormBuilder, private router: Router) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.pattern(this.emailPattern)]],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(this.passwordPattern),
        ],
      ],
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
        
        
      console.log('Login successful!', this.loginForm.value);
      this.router.navigate(['/home']);
    }
  }
}
