import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UnosNikotinaControllerService } from '../api';
import { ProizvodControllerService } from '../api';
import { UnosiZaKorisnikaURasponuDTO } from '../api/model/unosiZaKorisnikaURasponuDTO';
import { ProizvodDTO } from '../api/model/proizvodDTO';

@Component({
  standalone: true,
  selector: 'nicotine-history-page',
  imports: [CommonModule, FormsModule],
  template: `
    <div
      class="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-start"
    >
      <h1 class="text-3xl font-bold mb-6">Povijest unosa nikotina</h1>
      <!-- Edit Modal -->
      <div
        *ngIf="editFormVisible"
        style="background: rgba(31, 41, 55, 0.7)"
        class="fixed inset-0 flex items-center justify-center z-50"
      >
        <div
          class="bg-gray-800 p-8 rounded-lg shadow-lg w-full max-w-md relative border border-[#D2FF72] border-[1px]"
        >
          <button
            class="absolute top-2 right-2 text-gray-400 hover:text-white"
            (click)="closeEditForm()"
          >
            <span class="material-icons">close</span>
          </button>
          <h2 class="text-xl font-bold mb-4">Uredi unos</h2>
          <form (ngSubmit)="saveEdit()" class="space-y-4">
            <div>
              <label class="block mb-1">Datum</label>
              <input
                type="date"
                class="w-full p-2 rounded bg-gray-700 text-white"
                [(ngModel)]="editForm.datum"
                name="editDatum"
                required
              />
            </div>
            <div>
              <label class="block mb-1">Proizvod</label>
              <select
                class="w-full p-2 rounded bg-gray-700 text-white"
                [(ngModel)]="editForm.idProizvod"
                name="editProizvod"
                required
              >
                <option
                  *ngFor="let proizvod of productsList"
                  [value]="proizvod.idProizvod"
                >
                  {{ proizvod.opis }}
                </option>
              </select>
            </div>
            <div>
              <label class="block mb-1">Količina</label>
              <input
                type="number"
                class="w-full p-2 rounded bg-gray-700 text-white"
                [(ngModel)]="editForm.kolicina"
                name="editKolicina"
                min="1"
                required
              />
            </div>
            <div class="flex justify-end space-x-2">
              <button
                type="button"
                class="px-4 py-2 rounded bg-gray-600 hover:bg-gray-500 text-white"
                (click)="closeEditForm()"
              >
                Odustani
              </button>
              <button
                type="submit"
                class="px-4 py-2 rounded bg-[#D2FF72] hover:bg-[#73EC8B] text-gray-900 font-semibold"
              >
                Spremi
              </button>
            </div>
          </form>
        </div>
      </div>
      <form
        class="flex flex-row items-end justify-between mb-6 w-full max-w-2xl"
      >
        <div class="flex flex-row space-x-2 items-end">
          <div>
            <label class="block mb-1 text-gray-300">Od</label>
            <input
              type="date"
              class="p-2 rounded bg-gray-700 text-white"
              [(ngModel)]="dateFrom"
              name="dateFrom"
            />
          </div>
          <div>
            <label class="block mb-1 text-gray-300">Do</label>
            <input
              type="date"
              class="p-2 rounded bg-gray-700 text-white"
              [(ngModel)]="dateTo"
              name="dateTo"
            />
          </div>
          <button
            type="button"
            class="ml-2 p-2 rounded-full bg-gray-600 hover:bg-gray-500 text-white flex items-center justify-center"
            (click)="clearFilters()"
            title="Očisti filtere"
          >
            <span class="material-icons">close</span>
          </button>
        </div>
        <button
          type="button"
          class="py-2 px-4 rounded bg-[#D2FF72] hover:bg-[#73EC8B] text-gray-900 font-semibold transition"
          (click)="fetchHistory()"
        >
          Filtriraj
        </button>
      </form>
      <div class="w-full max-w-2xl">
        <table class="w-full text-left bg-gray-800 rounded-lg overflow-hidden">
          <thead>
            <tr class="bg-gray-700">
              <th class="p-3 text-center">Datum</th>
              <th class="p-3 text-center">Proizvod</th>
              <th class="p-3 text-center">Količina</th>
              <th class="p-3 text-center">Sadržaj nikotina</th>
              <th class="p-3 text-center">Akcije</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let unos of history">
              <td class="p-3 text-center">{{ formatDate(unos.datum) }}</td>
              <td class="p-3 text-center">
                {{ unos.opisProizvoda || unos.idProizvod }}
              </td>
              <td class="p-3 text-center">{{ unos.kolicina }}</td>
              <td class="p-3 text-center">
                {{
                  (unos.kolicina ? +unos.kolicina : 0) *
                    (unos.nikotinSadrzaj ? +unos.nikotinSadrzaj : 0)
                }}
                mg
              </td>
              <td class="p-3 text-center">
                <button
                  class="inline-flex items-center justify-center text-blue-400 hover:text-blue-600 mr-2"
                  (click)="editUnos(unos)"
                  title="Uredi"
                >
                  <span class="material-icons">edit</span>
                </button>
                <button
                  class="inline-flex items-center justify-center text-red-400 hover:text-red-600"
                  (click)="deleteUnos(unos)"
                  title="Obriši"
                >
                  <span class="material-icons">delete</span>
                </button>
              </td>
            </tr>
            <tr *ngIf="history.length === 0">
              <td colspan="3" class="p-3 text-center text-gray-400">
                Nema unosa za odabrani period.
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `,
})
export class NicotineHistoryPageComponent {
  private unosNikotinaService = inject(UnosNikotinaControllerService);
  private proizvodService = inject(ProizvodControllerService); // FIX: inject as class field
  history: UnosiZaKorisnikaURasponuDTO[] = [];
  dateFrom: string = '';
  dateTo: string = '';
  productsList: ProizvodDTO[] = [];
  editFormVisible = false;
  editForm: any = {};

  ngOnInit() {
    this.fetchProducts();
    this.fetchHistory();
  }

  fetchProducts() {
    this.proizvodService
      .sviProizvodi('body', false, { httpHeaderAccept: 'application/json' })
      .subscribe({
        next: (data) => (this.productsList = data),
        error: () => (this.productsList = []),
      });
  }

  fetchHistory() {
    // Always use unosiZaKorisnikaURasponu for fetching
    const today = new Date().toISOString().substring(0, 10) + 'T00:00:00';
    const from = this.dateFrom
      ? this.dateFrom + 'T00:00:00'
      : '2000-01-01T00:00:00';
    const to = this.dateTo ? this.dateTo + 'T00:00:00' : today;
    console.log('Fetching history from', from, 'to', to);
    this.unosNikotinaService.unosiZaKorisnikaURasponu(1, from, to).subscribe({
      next: (data) => {
        this.history = data;
        console.log('Fetched history:', data);
      },
      error: (err) => {
        this.history = [];
      },
    });
  }

  clearFilters() {
    this.dateFrom = '';
    this.dateTo = '';
    this.fetchHistory();
  }

  formatDate(dateStr: string | undefined): string {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    if (isNaN(d.getTime())) return dateStr;
    const day = d.getDate().toString().padStart(2, '0');
    const month = (d.getMonth() + 1).toString().padStart(2, '0');
    const year = d.getFullYear();
    return `${day}.${month}.${year}.`;
  }

  editUnos(unos: UnosiZaKorisnikaURasponuDTO) {
    this.editForm = {
      idUnosNikotina: unos.idUnosNikotina,
      datum: unos.datum ? unos.datum.substring(0, 10) : '',
      idProizvod: unos.idProizvod,
      kolicina: unos.kolicina,
    };
    this.editFormVisible = true;
  }

  closeEditForm() {
    this.editFormVisible = false;
    this.editForm = {};
  }

  saveEdit() {
    const updated = {
      idUnosNikotina: this.editForm.idUnosNikotina,
      datum: this.editForm.datum + 'T00:00:00',
      idProizvod: this.editForm.idProizvod,
      kolicina: this.editForm.kolicina,
    };
    this.unosNikotinaService
      .azurirajUnosNikotina(updated.idUnosNikotina, updated)
      .subscribe({
        next: () => {
          this.closeEditForm();
          this.fetchHistory();
        },
        error: () => {
          alert('Greška pri spremanju izmjena!');
        },
      });
  }

  deleteUnos(unos: UnosiZaKorisnikaURasponuDTO) {
    if (confirm('Jeste li sigurni da želite obrisati ovaj unos?')) {
      this.unosNikotinaService
        .obrisiUnosNikotina(unos.idUnosNikotina!)
        .subscribe({
          next: () => {
            this.fetchHistory();
          },
          error: () => {
            alert('Greška pri brisanju unosa!');
          },
        });
    }
  }
}
