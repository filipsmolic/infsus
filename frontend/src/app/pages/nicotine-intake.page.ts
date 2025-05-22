import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProizvodControllerService } from '../api';
import { UnosNikotinaControllerService } from '../api';
import { ProizvodDTO } from '../api/model/proizvodDTO';
import { UnosNikotinaDTO } from '../api/model/unosNikotinaDTO';
import { BehaviorSubject } from 'rxjs';

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
              <select
                class="w-full p-3 rounded bg-gray-700 text-white"
                [ngModel]="entry.productId"
                [name]="'product' + i"
                required
                (change)="onProductChange(i, $event)"
              >
                <option value="" disabled selected>Odaberi proizvod</option>
                <option
                  *ngFor="let proizvod of productsList"
                  [value]="proizvod.idProizvod"
                >
                  {{ proizvod.opis }}
                </option>
              </select>
            </div>
            <div class="w-32">
              <label class="block mb-1 text-gray-300">Količina</label>
              <input
                type="number"
                class="w-full p-3 rounded bg-gray-700 text-white"
                [ngModel]="entry.quantity"
                [name]="'quantity' + i"
                min="1"
                (input)="onQuantityChange(i, $event)"
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
  private proizvodService = inject(ProizvodControllerService);
  private unosNikotinaService = inject(UnosNikotinaControllerService);

  date: string = new Date().toISOString().substring(0, 10);
  products$ = new BehaviorSubject<
    { productId: number | undefined; quantity: number }[]
  >([{ productId: undefined, quantity: 0 }]);
  totalQuantity = 0;
  productsList: ProizvodDTO[] = [];

  ngOnInit(): void {
    this.fetchProducts();
    this.products$.subscribe((products) => {
      this.totalQuantity = products.reduce((sum, p) => {
        const prod = this.productsList.find(
          (pr) => pr.idProizvod === p.productId
        );
        return (
          sum +
          (prod
            ? Number(p.quantity || 0) * Number(prod.nikotinSadrzaj || 0)
            : 0)
        );
      }, 0);
    });
  }

  get products() {
    return this.products$.value;
  }

  set products(val) {
    this.products$.next(val);
  }

  fetchProducts(): void {
    this.proizvodService
      .sviProizvodi('body', false, { httpHeaderAccept: 'application/json' })
      .subscribe({
        next: (data) => {
          this.productsList = data;
        },
        error: (err) => {
          console.error('Error fetching products', err);
        },
      });
  }

  addProduct() {
    this.products = [...this.products, { productId: undefined, quantity: 0 }];
  }

  removeProduct(i: number) {
    const updated = [...this.products];
    updated.splice(i, 1);
    this.products = updated;
  }

  onProductChange(i: number, event: Event) {
    const value = Number((event.target as HTMLSelectElement).value);
    const updated = [...this.products];
    updated[i].productId = value;
    this.products = updated;
  }

  onQuantityChange(i: number, event: Event) {
    const value = Number((event.target as HTMLInputElement).value);
    const updated = [...this.products];
    updated[i].quantity = value;
    this.products = updated;
  }

  submit() {
    const unosList = this.products.map((entry) => {
      return {
        datum: this.date,
        kolicina: entry.quantity,
        idProizvod: entry.productId,
      };
    });
    unosList.forEach((unos) => {
      this.unosNikotinaService.dodajUnosNikotina(unos).subscribe({
        next: () => {},
        error: (err) => {
          console.error('Error saving nicotine intake', err);
        },
      });
    });
    alert('Unos spremljen!');
  }
}
