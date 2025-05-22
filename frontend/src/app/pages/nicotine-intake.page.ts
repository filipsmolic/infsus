import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'nicotine-intake-page',
  imports: [CommonModule, FormsModule],
  template: `
    <div
      class="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-start"
    >
      <h1 class="text-3xl font-bold mb-6">Unos nikotina</h1>
      <form
        (ngSubmit)="submit()"
        class="bg-gray-800 p-10 rounded-2xl shadow w-full max-w-2xl space-y-6"
      >
        <div
          class="flex flex-col md:flex-row md:space-x-6 space-y-4 md:space-y-0"
        >
          <div class="flex-1">
            <label class="block mb-1 text-gray-300">Količina (mg)</label>
            <input
              type="number"
              class="w-full p-3 rounded bg-gray-700 text-white opacity-60 cursor-not-allowed"
              [value]="totalQuantity"
              readonly
            />
          </div>
          <div class="flex-1">
            <label class="block mb-1 text-gray-300">Datum</label>
            <input
              type="date"
              class="w-full p-3 rounded bg-gray-700 text-white"
              [(ngModel)]="date"
              name="date"
            />
          </div>
        </div>
        <hr class="my-4 border-gray-600" />
        <div class="space-y-4">
          <div
            *ngFor="let entry of products; let i = index"
            class="flex flex-col md:flex-row md:space-x-4 space-y-2 md:space-y-0 items-end"
          >
            <div class="flex-1">
              <label class="block mb-1 text-gray-300">Proizvod</label>
              <input
                type="text"
                class="w-full p-3 rounded bg-gray-700 text-white"
                [(ngModel)]="entry.product"
                [name]="'product' + i"
                placeholder="Naziv proizvoda"
                required
              />
            </div>
            <div class="w-32">
              <label class="block mb-1 text-gray-300">Količina</label>
              <input
                type="number"
                class="w-full p-3 rounded bg-gray-700 text-white"
                [(ngModel)]="entry.quantity"
                [name]="'quantity' + i"
                min="1"
                (input)="updateTotal()"
                required
              />
            </div>
            <button
              type="button"
              class="ml-2 text-red-400 hover:text-red-600"
              (click)="removeProduct(i)"
              *ngIf="products.length > 1"
            >
              <span class="material-icons">remove_circle</span>
            </button>
          </div>
        </div>
        <div
          class="flex flex-col md:flex-row md:space-x-4 space-y-2 md:space-y-0"
        >
          <button
            type="button"
            class="flex-1 py-3 rounded bg-[#73EC8B] hover:bg-[#D2FF72] text-gray-900 font-semibold flex items-center justify-center transition"
            (click)="addProduct()"
          >
            <span class="material-icons mr-2">add</span> Dodaj proizvod
          </button>
          <button
            type="submit"
            class="flex-1 py-3 rounded bg-[#D2FF72] hover:bg-[#73EC8B] text-gray-900 font-semibold transition"
          >
            Spremi unos
          </button>
        </div>
      </form>
    </div>
  `,
})
export class NicotineIntakePageComponent {
  date: string = new Date().toISOString().substring(0, 10);
  products = [{ product: '', quantity: 0 }];
  totalQuantity = 0;

  addProduct() {
    this.products.push({ product: '', quantity: 0 });
  }

  removeProduct(i: number) {
    this.products.splice(i, 1);
    this.updateTotal();
  }

  updateTotal() {
    this.totalQuantity = this.products.reduce(
      (sum, p) => sum + Number(p.quantity || 0),
      0
    );
  }

  submit() {
    // Handle form submission
    alert('Unos spremljen!');
  }
}
