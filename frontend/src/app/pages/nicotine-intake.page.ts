import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ProizvodControllerService } from '../api';
import { UnosNikotinaControllerService } from '../api';
import { ProizvodDTO } from '../api/model/proizvodDTO';
import { UnosNikotinaDTO } from '../api/model/unosNikotinaDTO';
import { BatchUnosNikotinaDTO } from '../api/model/batchUnosNikotinaDTO';
import { ProizvodUnosDTO } from '../api/model/proizvodUnosDTO';
import { BehaviorSubject } from 'rxjs';

@Component({
  standalone: true,
  selector: 'nicotine-intake-page',
  imports: [CommonModule, FormsModule],
  template: `
    <div
      class="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-start pt-10"
    >
      <h1 class="text-3xl font-bold mb-6">Unos nikotina</h1>
      <form
        #intakeForm="ngForm"
        (ngSubmit)="submit(intakeForm)"
        class="bg-gray-800 p-10 rounded-2xl shadow w-full max-w-2xl space-y-6"
      >
        <div
          class="flex flex-col md:flex-row md:space-x-6 space-y-4 md:space-y-0"
        >
          <div class="flex-1">
            <label for="totalQuantityInput" class="block mb-1 text-gray-300"
              >Ukupni nikotin (mg)</label
            >
            <input
              id="totalQuantityInput"
              type="number"
              class="w-full p-3 rounded bg-gray-700 text-white opacity-60 cursor-not-allowed"
              [value]="totalQuantity"
              readonly
            />
          </div>
          <div class="flex-1">
            <label for="dateInput" class="block mb-1 text-gray-300"
              >Datum</label
            >
            <input
              id="dateInput"
              type="date"
              class="w-full p-3 rounded bg-gray-700 text-white"
              [(ngModel)]="date"
              name="date"
              required
              [max]="maxDate"
              #dateModel="ngModel"
            />
            <div
              *ngIf="
                dateModel.invalid && (dateModel.dirty || dateModel.touched)
              "
              class="text-red-400 text-sm mt-1"
            >
              <small *ngIf="dateModel.errors?.['required']"
                >Datum je obavezan.</small
              >
              <small *ngIf="dateModel.errors?.['max']"
                >Datum ne može biti u budućnosti.</small
              >
            </div>
          </div>
        </div>
        <hr class="my-4 border-gray-600" />
        <div class="space-y-4">
          <div
            *ngFor="let entry of products; let i = index"
            class="flex md:flex-row md:space-x-4 space-y-2 md:space-y-0 items-start"
          >
            <div class="flex-1">
              <label
                [for]="'productSelect' + i"
                class="block mb-1 text-gray-300"
                >Proizvod</label
              >
              <select
                name="product"
                [id]="'productSelect' + i"
                class="w-full p-3 rounded bg-gray-700 text-white"
                [(ngModel)]="entry.productId"
                [name]="'product' + i"
                required
                #productModel="ngModel"
                (change)="onProductChange(i, $event)"
              >
                <option [ngValue]="undefined" disabled selected>
                  Odaberi proizvod
                </option>
                <option
                  *ngFor="let proizvod of productsList"
                  [value]="proizvod.idProizvod"
                >
                  {{ proizvod.opis }}
                </option>
              </select>
              <div
                *ngIf="
                  productModel.invalid &&
                  (productModel.dirty || productModel.touched)
                "
                class="text-red-400 text-sm mt-1"
              >
                <small *ngIf="productModel.errors?.['required']"
                  >Odabir proizvoda je obavezan.</small
                >
              </div>
            </div>
            <div class="w-32">
              <label
                [for]="'quantityInput' + i"
                class="block mb-1 text-gray-300"
                >Količina</label
              >
              <input
                name="quantity"
                [id]="'quantityInput' + i"
                type="number"
                class="w-full p-3 rounded bg-gray-700 text-white"
                [(ngModel)]="entry.quantity"
                [name]="'quantity' + i"
                min="1"
                required
                #quantityModel="ngModel"
                (input)="onQuantityChange(i, $event)"
              />
              <div
                *ngIf="
                  quantityModel.invalid &&
                  (quantityModel.dirty || quantityModel.touched)
                "
                class="text-red-400 text-sm mt-1"
              >
                <small *ngIf="quantityModel.errors?.['required']"
                  >Količina je obavezna.</small
                >
                <small *ngIf="quantityModel.errors?.['min']"
                  >Količina mora biti barem 1.</small
                >
              </div>
            </div>
            <button
              type="button"
              class="ml-2 text-red-400 hover:text-red-600 flex items-center justify-center h-12 w-12 rounded-full bg-gray-700 hover:bg-gray-600 transition-colors"
              (click)="removeProduct(i)"
              *ngIf="products.length > 1"
              aria-label="Ukloni proizvod"
            >
              <span class="material-icons">remove_circle_outline</span>
            </button>
          </div>
        </div>
        <div
          class="flex flex-col md:flex-row md:space-x-4 space-y-2 md:space-y-0 pt-4"
        >
          <button
            type="button"
            class="flex-1 py-3 rounded bg-[#73EC8B] hover:bg-[#5dbd72] text-gray-900 font-semibold flex items-center justify-center transition"
            (click)="addProduct()"
          >
            <span class="material-icons mr-2">add_circle_outline</span> Dodaj
            proizvod
          </button>
          <button
            type="submit"
            class="flex-1 py-3 rounded bg-[#D2FF72] hover:bg-[#b8e05a] text-gray-900 font-semibold transition"
            [disabled]="intakeForm.invalid"
          >
            Spremi unos
          </button>
        </div>
        <div class="flex justify-center mt-4 h-5">
          @if (isSaved) {
          <small class="text-green-400"> Unos uspješno spremljen! </small>
          } @else if (isError) {
          <small class="text-red-400">
            Pogreška prilikom spremanja unosa:
            {{ saveError || 'Pokušajte ponovno.' }}
          </small>
          }
        </div>
      </form>
    </div>
  `,
})
export class NicotineIntakePageComponent implements OnInit {
  private proizvodService = inject(ProizvodControllerService);
  private unosNikotinaService = inject(UnosNikotinaControllerService);

  date: string = new Date().toISOString().substring(0, 10);
  maxDate: string = '';
  products$ = new BehaviorSubject<
    { productId: number | undefined; quantity: number }[]
  >([{ productId: undefined, quantity: 1 }]);
  totalQuantity = 0;
  productsList: ProizvodDTO[] = [];

  public isSaved = false;
  public isError = false;
  public saveError: string | null = null;

  ngOnInit(): void {
    this.maxDate = new Date().toISOString().substring(0, 10);
    this.fetchProducts();
    this.products$.subscribe((products) => {
      this.calculateTotalNicotine(products);
    });
  }

  calculateTotalNicotine(
    products: { productId: number | undefined; quantity: number }[]
  ): void {
    this.totalQuantity = products.reduce((sum, p) => {
      const prod = this.productsList.find(
        (pr) => pr.idProizvod === p.productId
      );
      return (
        sum +
        (prod ? Number(p.quantity || 0) * Number(prod.nikotinSadrzaj || 0) : 0)
      );
    }, 0);
  }

  get products() {
    return this.products$.value;
  }

  set products(val: { productId: number | undefined; quantity: number }[]) {
    this.products$.next(val);
  }

  fetchProducts(): void {
    this.proizvodService
      .sviProizvodi('body', false, { httpHeaderAccept: 'application/json' })
      .subscribe({
        next: (data) => {
          this.productsList = data;
          this.calculateTotalNicotine(this.products);
        },
        error: (err) => {
          console.error('Error fetching products', err);
        },
      });
  }

  addProduct() {
    this.products = [...this.products, { productId: undefined, quantity: 1 }];
  }

  removeProduct(i: number) {
    const updated = [...this.products];
    updated.splice(i, 1);
    this.products = updated;
  }

  onProductChange(i: number, event: Event) {
    const value = Number((event.target as HTMLSelectElement).value);
    const updated = [...this.products];
    if (updated[i]) {
      updated[i].productId = value;
      this.products = updated;
    }
  }

  onQuantityChange(i: number, event: Event) {
    const value = Number((event.target as HTMLInputElement).value);
    const updated = [...this.products];
    if (updated[i]) {
      updated[i].quantity = value;
      this.products = updated;
    }
  }

  submit(form: NgForm) {
    if (form.invalid) {
      this.isError = true;
      this.saveError = 'Molimo ispravite greške u formi.';
      Object.keys(form.controls).forEach((key) => {
        form.controls[key].markAsTouched();
      });
      return;
    }
    this.isSaved = false;
    this.isError = false;
    this.saveError = null;

    const today = new Date();
    today.setHours(23, 59, 59, 999);
    const selectedDate = new Date(this.date + 'T00:00:00');

    if (selectedDate > today) {
      this.isError = true;
      this.saveError = 'Datum unosa ne može biti u budućnosti.';
      return;
    }

    const proizvodi: ProizvodUnosDTO[] = this.products
      .filter((entry) => entry.productId !== undefined && entry.quantity > 0)
      .map((entry) => {
        return {
          idProizvod: entry.productId,
          kolicina: entry.quantity,
        };
      });

    if (proizvodi.length === 0) {
      this.isError = true;
      this.saveError =
        'Potrebno je dodati barem jedan proizvod s količinom većom od 0.';
      return;
    }

    const batch: BatchUnosNikotinaDTO = {
      idKorisnik: 1,
      datum: this.date,
      proizvodi: proizvodi,
    };

    console.log('Batch to save:', batch);
    this.unosNikotinaService
      .batchUnosNikotina(batch, 'body', false, { httpHeaderAccept: '*/*' })
      .subscribe({
        next: () => {
          this.products = [{ productId: undefined, quantity: 1 }];
          this.date = new Date().toISOString().substring(0, 10);
          this.maxDate = this.date;
          this.isSaved = true;
          form.resetForm({
            date: this.date,
          });
        },
        error: (err) => {
          this.isError = true;
          this.saveError =
            err.error?.message ||
            err.message ||
            'Došlo je do greške prilikom spremanja.';
          console.error('Error saving batch:', err);
        },
      });
  }
}
