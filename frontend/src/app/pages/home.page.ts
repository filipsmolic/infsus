import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import Chart from 'chart.js/auto';
import { UnosNikotinaControllerService } from '../api/api/unosNikotinaController.service';
import { UnosKupnjeControllerService } from '../api/api/unosKupnjeController.service';
import { UnosNikotinaDTO } from '../api/model/unosNikotinaDTO';
import { UnosKupnjeDTO } from '../api/model/unosKupnjeDTO';

// Prošireni tipovi koji uključuju datum
interface UnosNikotinaExtended extends UnosNikotinaDTO {
  datum?: string;
}

interface UnosKupnjeExtended extends UnosKupnjeDTO {
  datum?: string;
}

@Component({
  standalone: true,
  selector: 'home-page',
  imports: [CommonModule, RouterModule],
  template: `
    <div class="container mx-auto px-4 py-8 max-w-4xl">
      <div class="text-center mb-6">
        <h2 class="text-4xl font-bold" style="color: #D2FF72;">
          {{ totalTodayNicotine }} <span class="text-xl font-normal text-white">mg</span>
        </h2>
        <p class="text-lg text-gray-300">danas</p>
      </div>

      <div class="space-y-6 width-full flex flex-col items-center">
        <div class="w-full">
          <h3 class="mb-2 text-xl font-semibold" style="color: #D2FF72;">Tjedni unos nikotina</h3>
          <div
            class="h-72 bg-gray-700 rounded-md p-4"
          >
            <canvas #nicotineChart></canvas>
          </div>
        </div>
        <div class="w-full">
          <h3 class="mb-2 text-xl font-semibold" style="color: #D2FF72;">Tjedna potrošnja na kupnju</h3>
          <div
            class="h-72 bg-gray-700 rounded-md p-4"
          >
            <canvas #purchaseChart></canvas>
          </div>
        </div>
      </div>
    </div>
  `,
})
export class HomePageComponent implements OnInit, AfterViewInit {
  @ViewChild('nicotineChart') nicotineChartRef!: ElementRef;
  @ViewChild('purchaseChart') purchaseChartRef!: ElementRef;
  
  private unosNikotinaService = inject(UnosNikotinaControllerService);
  private unosKupnjeService = inject(UnosKupnjeControllerService);
  
  nicotineData: UnosNikotinaExtended[] = [];
  purchaseData: UnosKupnjeExtended[] = [];
  nicotineChart: any | null = null;
  purchaseChart: any | null = null;
  totalTodayNicotine: number = 0;
  
  ngOnInit(): void {
    this.fetchData();
  }
  
  ngAfterViewInit(): void {
    // Grafovi će se inicijalizirati nakon dohvata podataka
  }
  
  fetchData(): void {
    // Dohvati podatke za zadnjih 7 dana
    const today = new Date();
    const weekAgo = new Date();
    weekAgo.setDate(today.getDate() - 7);
    
    const todayStr = today.toISOString();
    const weekAgoStr = weekAgo.toISOString();
    
    console.log("Fetching data for date range:", weekAgoStr, "to", todayStr);
    
    // Dohvati unos nikotina za korisnika s id=1
    this.unosNikotinaService.unosiZaKorisnikaURasponu(
      1,
      weekAgoStr,
      todayStr
    ).subscribe({
      next: (response) => {
        console.log("Nicotine data received:", response);
        
        // Obrada odgovora, koji može biti Page ili direktna lista
        if (response && typeof response === 'object' && 'content' in response) {
          this.nicotineData = response.content as UnosNikotinaExtended[];
        } else {
          this.nicotineData = response as unknown as UnosNikotinaExtended[];
        }
        
        console.log("Processed nicotine data:", this.nicotineData);
        
        this.calculateTodayNicotine();
        setTimeout(() => {
          this.initNicotineChart();
        }, 100);
      },
      error: (error) => {
        console.error('Error fetching nicotine data:', error);
        // U slučaju greške, prikaži prazne podatke
        this.initNicotineChart();
      }
    });
    
    // Dohvati podatke o kupnji za korisnika s id=1
    this.unosKupnjeService.unosiKupnjeZaKorisnikaURasponu(
      1,
      weekAgoStr,
      todayStr
    ).subscribe({
      next: (data) => {
        console.log("Purchase data received:", data);
        
        if (data && typeof data === 'object' && 'content' in data) {
          this.purchaseData = data.content as UnosKupnjeExtended[];
        } else {
          this.purchaseData = data as unknown as UnosKupnjeExtended[];
        }
        
        console.log("Processed purchase data:", this.purchaseData);
        
        setTimeout(() => {
          this.initPurchaseChart();
        }, 100);
      },
      error: (error) => {
        console.error('Error fetching purchase data:', error);
        // U slučaju greške, prikaži prazne podatke
        this.initPurchaseChart();
      }
    });
  }
  
  calculateTodayNicotine(): void {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    // Filtriraj samo današnje unose
    const todayEntries = this.nicotineData.filter(entry => {
      if (!entry.datum) return false;
      
      const entryDate = new Date(entry.datum);
      const entryDay = new Date(entryDate);
      entryDay.setHours(0, 0, 0, 0);
      return entryDay.getTime() === today.getTime();
    });
    
    // Zbroji ukupnu količinu nikotina za danas
    this.totalTodayNicotine = todayEntries.reduce((sum, entry) => sum + (entry.kolicina || 0), 0);
  }
  
  initNicotineChart(): void {
    console.log('Initializing nicotine chart with data:', this.nicotineData);
    
    if (!this.nicotineChartRef || !this.nicotineChartRef.nativeElement) {
      console.error('Nicotine chart canvas element not available');
      return;
    }
    
    const ctx = this.nicotineChartRef.nativeElement.getContext('2d');
    if (!ctx) {
      console.error('Failed to get 2d context from canvas');
      return;
    }
    
    // Grupiraj podatke po danima
    const last7Days = this.getLast7Days();
    console.log('Last 7 days:', last7Days);
    
    const nicotineByDay = this.groupDataByDay(this.nicotineData, 'kolicina');
    console.log('Nicotine data by day:', nicotineByDay);
    
    const chartData = last7Days.map(day => {
      const dayStr = day.toISOString().split('T')[0];
      return nicotineByDay[dayStr] || 0;
    });
    console.log('Chart data points:', chartData);
    
    // Nazivi dana u tjednu
    const labels = last7Days.map(date => {
      const dayNames = ['Ned', 'Pon', 'Uto', 'Sri', 'Čet', 'Pet', 'Sub'];
      return dayNames[date.getDay()];
    });
    
    if (this.nicotineChart) {
      console.log('Destroying old chart');
      this.nicotineChart.destroy();
    }
    
    try {
      console.log('Creating new chart with configuration:', {
        type: 'line',
        data: {
          labels,
          datasets: [{
            label: 'Unos nikotina (mg)',
            data: chartData,
          }]
        }
      });
      
      this.nicotineChart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [{
            label: 'Unos nikotina (mg)',
            data: chartData,
            borderColor: '#73EC8B',
            backgroundColor: 'rgba(115, 236, 139, 0.2)',
            tension: 0.4,
            fill: true
          }]
        },
        options: {
          responsive: true,
          scales: {
            y: {
              beginAtZero: true,
              grid: {
                color: 'rgba(255, 255, 255, 0.1)'
              },
              ticks: {
                color: '#ffffff'
              }
            },
            x: {
              grid: {
                color: 'rgba(255, 255, 255, 0.1)'
              },
              ticks: {
                color: '#ffffff'
              }
            }
          },
          plugins: {
            legend: {
              labels: {
                color: '#ffffff'
              }
            }
          }
        }
      });
      console.log('Chart created successfully:', this.nicotineChart);
    } catch (error) {
      console.error('Error creating chart:', error);
    }
  }
  
  initPurchaseChart(): void {
    console.log('Initializing purchase chart with data:', this.purchaseData);
    
    if (!this.purchaseChartRef || !this.purchaseChartRef.nativeElement) {
      console.error('Purchase chart canvas element not available');
      return;
    }
    
    const ctx = this.purchaseChartRef.nativeElement.getContext('2d');
    if (!ctx) {
      console.error('Failed to get 2d context from canvas');
      return;
    }
    
    // Grupiraj podatke po danima
    const last7Days = this.getLast7Days();
    const purchaseByDay = this.groupDataByDay(this.purchaseData, 'cijena');
    console.log('Purchase data by day:', purchaseByDay);
    
    const chartData = last7Days.map(day => {
      const dayStr = day.toISOString().split('T')[0];
      return purchaseByDay[dayStr] || 0;
    });
    console.log('Chart data points:', chartData);
    
    // Nazivi dana u tjednu
    const labels = last7Days.map(date => {
      const dayNames = ['Ned', 'Pon', 'Uto', 'Sri', 'Čet', 'Pet', 'Sub'];
      return dayNames[date.getDay()];
    });
    
    if (this.purchaseChart) {
      console.log('Destroying old chart');
      this.purchaseChart.destroy();
    }
    
    try {
      this.purchaseChart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: 'Potrošnja (€)',
            data: chartData,
            backgroundColor: '#D2FF72',
            borderColor: 'rgba(210, 255, 114, 0.8)',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          scales: {
            y: {
              beginAtZero: true,
              grid: {
                color: 'rgba(255, 255, 255, 0.1)'
              },
              ticks: {
                color: '#ffffff'
              }
            },
            x: {
              grid: {
                color: 'rgba(255, 255, 255, 0.1)'
              },
              ticks: {
                color: '#ffffff'
              }
            }
          },
          plugins: {
            legend: {
              labels: {
                color: '#ffffff'
              }
            }
          }
        }
      });
      console.log('Chart created successfully:', this.purchaseChart);
    } catch (error) {
      console.error('Error creating chart:', error);
    }
  }
  
  getLast7Days(): Date[] {
    const result: Date[] = [];
    for (let i = 6; i >= 0; i--) {
      const date = new Date();
      date.setDate(date.getDate() - i);
      date.setHours(0, 0, 0, 0);
      result.push(date);
    }
    return result;
  }
  
  groupDataByDay<T extends { datum?: string }>(data: T[], valueField: keyof T): Record<string, number> {
    const result: Record<string, number> = {};
    
    data.forEach(item => {
      if (item.datum) {
        const date = new Date(item.datum);
        const dayStr = date.toISOString().split('T')[0];
        
        if (!result[dayStr]) {
          result[dayStr] = 0;
        }
        
        // Zbrajamo vrijednosti za taj dan
        const value = item[valueField] as unknown as number || 0;
        result[dayStr] += value;
      }
    });
    
    return result;
  }
}
