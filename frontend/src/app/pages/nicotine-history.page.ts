import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-nicotine-history-page',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container">
      <h1>Nicotine History</h1>
      <p>Your nicotine consumption history will be displayed here.</p>
    </div>
  `,
  styles: [`
    .container {
      padding: 20px;
      max-width: 1200px;
      margin: 0 auto;
    }
    
    h1 {
      margin-bottom: 20px;
    }
  `]
})
export class NicotineHistoryPageComponent {
  constructor() {}
}
