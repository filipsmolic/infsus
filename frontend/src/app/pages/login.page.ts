import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="login-container">
      <div class="login-box">
        <div class="app-logo">NicotiNO</div>
        <h2>Login</h2>
        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" formControlName="email" placeholder="Enter your email">
            <div class="error-message" *ngIf="loginForm.get('email')?.invalid && (loginForm.get('email')?.dirty || loginForm.get('email')?.touched)">
              <span *ngIf="loginForm.get('email')?.errors?.['required']">Email is required.</span>
              <span *ngIf="loginForm.get('email')?.errors?.['pattern']">Please enter a valid email address.</span>
            </div>
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" formControlName="password" placeholder="Enter your password">
            <div class="error-message" *ngIf="loginForm.get('password')?.invalid && (loginForm.get('password')?.dirty || loginForm.get('password')?.touched)">
              <span *ngIf="loginForm.get('password')?.errors?.['required']">Password is required.</span>
              <span *ngIf="loginForm.get('password')?.errors?.['minlength']">Password must be at least 8 characters.</span>
              <span *ngIf="loginForm.get('password')?.errors?.['pattern']">
                Password must contain at least one uppercase letter, one lowercase letter, one number and one special character.
              </span>
            </div>
          </div>
          <button type="submit" [disabled]="loginForm.invalid">Login</button>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
      background-color: #1f1f1f;
    }
    
    .login-box {
      width: 400px;
      padding: 40px;
      background-color: #2d2d2d;
      border-radius: 8px;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
    }
    
    .app-logo {
      text-align: center;
      font-size: 32px;
      font-weight: bold;
      color: #73EC8B;
      margin-bottom: 20px;
      text-shadow: 0 0 10px rgba(115, 236, 139, 0.5);
    }
    
    h2 {
      text-align: center;
      margin-bottom: 30px;
      color: #73EC8B;
      font-weight: bold;
    }
    
    .form-group {
      margin-bottom: 20px;
    }
    
    label {
      display: block;
      margin-bottom: 8px;
      font-weight: 500;
      color: #ffffff;
    }
    
    input {
      width: 100%;
      padding: 12px;
      border: 1px solid #444;
      background-color: #333;
      color: #fff;
      border-radius: 4px;
      font-size: 16px;
    }
    
    input::placeholder {
      color: #aaa;
    }
    
    .error-message {
      color: #ff6b6b;
      margin-top: 5px;
      font-size: 14px;
    }
    
    button {
      width: 100%;
      padding: 12px;
      background-color: #73EC8B;
      color: #1f1f1f;
      border: none;
      border-radius: 4px;
      font-size: 16px;
      font-weight: bold;
      cursor: pointer;
      transition: background-color 0.3s;
    }
    
    button:hover {
      background-color: #62cb76;
    }
    
    button:disabled {
      background-color: #444444;
      color: #888888;
      cursor: not-allowed;
    }
  `]
})
export class LoginPageComponent {
  loginForm: FormGroup;
  
  // Regular expression for email validation
  private emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  
  // Regular expression for password validation (at least 8 characters, one uppercase letter,
  // one lowercase letter, one number, and one special character)
  private passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
  
  constructor(private fb: FormBuilder, private router: Router) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.pattern(this.emailPattern)]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(this.passwordPattern)
      ]]
    });
  }
  
  onSubmit() {
    if (this.loginForm.valid) {
      // In a real application, you would call an authentication service here
      // For this dummy implementation, we'll just navigate to the home page
      console.log('Login successful!', this.loginForm.value);
      this.router.navigate(['/home']);
    }
  }
} 