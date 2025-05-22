import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true,
  selector: 'home-page',
  imports: [CommonModule, RouterModule],
  template: `
    <div>
      <div class="text-center mb-6">
        <h2 class="text-4xl font-bold" style="color: #D2FF72;">
          48 <span class="text-xl font-normal text-white">mg</span>
        </h2>
        <p class="text-lg text-gray-300">danas</p>
      </div>

      <div class="space-y-6 width-full flex flex-col items-center">
        <div class="w-200">
          <h3 class="mb-2" style="color: #D2FF72;">Tjedni unos</h3>
          <div
            class="h-32 bg-gray-700 rounded-md flex items-center justify-center"
          >
            <span style="color: #D2FF72;">[Chart here]</span>
          </div>
        </div>
        <div class="w-200">
          <h3 class="mb-2" style="color: #D2FF72;">Tjedna potrošnja</h3>
          <div
            class="h-32 bg-gray-700 rounded-md flex items-center justify-center"
          >
            <span style="color: #D2FF72;">[Chart here]</span>
          </div>
        </div>
      </div>
    </div>
  `,
})
export class HomePageComponent {
  logNewEntry() {
    console.log('New nicotine entry triggered');
    // Trigger form, modal, or route
  }
}
