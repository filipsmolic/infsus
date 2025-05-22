import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UnosNikotinaControllerService } from '../api';
import { UnosiZaKorisnikaURasponuDTO } from '../api/model/unosiZaKorisnikaURasponuDTO';

@Component({
  standalone: true,
  selector: 'nicotine-history-page',
  imports: [CommonModule, FormsModule],
  template: `
    <div
      class="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-start"
    >
      <h1 class="text-3xl font-bold mb-6">Povijest unosa nikotina</h1>
      <form
        class="flex flex-row items-end justify-between mb-6 w-full max-w-2xl"
      >
        <div class="flex flex-row space-x-4">
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
              <th class="p-3">Datum</th>
              <th class="p-3">Proizvod</th>
              <th class="p-3">Količina</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let unos of history">
              <td class="p-3">{{ unos.datum }}</td>
              <td class="p-3">{{ unos.opisProizvoda || unos.idProizvod }}</td>
              <td class="p-3">{{ unos.kolicina }}</td>
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
  history: UnosiZaKorisnikaURasponuDTO[] = [];
  dateFrom: string = '';
  dateTo: string = '';

  ngOnInit() {
    this.fetchHistory();
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
}
