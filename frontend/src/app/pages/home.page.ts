import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import Chart from 'chart.js/auto';
import { UnosNikotinaControllerService } from '../api/api/unosNikotinaController.service';
import { UnosNikotinaDTO } from '../api/model/unosNikotinaDTO';

// Prošireni tipovi koji uključuju datum
interface UnosNikotinaExtended extends UnosNikotinaDTO {
  datum?: string;
  nikotinSadrzaj?: number;
  opisProizvoda?: string;
}

@Component({
  standalone: true,
  selector: 'home-page',
  imports: [CommonModule, RouterModule],
  template: `
    <div class="container mx-auto px-4 py-8 max-w-6xl">
      <div class="text-center mb-6">
        <h2 class="text-7xl font-bold" style="color: #D2FF72;">
          {{ totalTodayNicotine }} <span class="text-xl font-normal text-white">mg</span>
        </h2>
        <p class="text-lg text-gray-300">danas</p>
      </div>

      <div class="space-y-6 width-full flex flex-col items-center">
        <div class="w-full">
          <h3 class="mb-2 text-xl font-semibold" style="color: #D2FF72;">Tjedni unos nikotina</h3>
          <div
            class="h-80 bg-gray-700 rounded-md p-2 w-full"
          >
            <canvas #nicotineChart></canvas>
          </div>
        </div>
      </div>
    </div>
  `,
})
export class HomePageComponent implements OnInit, AfterViewInit {
  @ViewChild('nicotineChart') nicotineChartRef!: ElementRef;
  
  private unosNikotinaService = inject(UnosNikotinaControllerService);
  
  nicotineData: UnosNikotinaExtended[] = [];
  nicotineChart: any | null = null;
  totalTodayNicotine: number = 0;
  
  ngOnInit(): void {
    this.fetchData();
  }
  
  ngAfterViewInit(): void {
    // Grafovi će se inicijalizirati nakon dohvata podataka
  }
  
  fetchData(): void {
    const today = new Date(); // Get current date and time
    today.setHours(23, 59, 59, 999); // Set time to end of the day

    const sevenDaysAgo = new Date(today);
    sevenDaysAgo.setDate(today.getDate() - 6); // Go back 6 days to get a 7-day range including today
    sevenDaysAgo.setHours(0, 0, 0, 0); // Set time to start of that day
    
    const todayStr = today.toISOString();
    const sevenDaysAgoStr = sevenDaysAgo.toISOString();
    
    console.log("Date range for nicotine data:", sevenDaysAgoStr, "to", todayStr);
    
    // Dohvati unos nikotina za korisnika s id=1
    this.unosNikotinaService.unosiZaKorisnikaURasponu(
      1,
      sevenDaysAgoStr,
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
        
        this.calculateTodayNicotine();
        setTimeout(() => {
          this.initNicotineChart();
        }, 100);
      },
      error: (error) => {
        console.error('Error fetching nicotine data:', error);
        this.initNicotineChart();
      }
    });
  }
  
  calculateTodayNicotine(): void {
    const todayStart = new Date(); 
    todayStart.setHours(0, 0, 0, 0); // Start of today

    const todayEnd = new Date();
    todayEnd.setHours(23, 59, 59, 999); // End of today
    
    // Filtriraj samo današnje unose
    const todayEntries = this.nicotineData.filter(entry => {
      if (!entry.datum) return false;
      
      const entryDate = new Date(entry.datum);
      // No need to normalize entryDate to start of day if comparing against a range
      return entryDate >= todayStart && entryDate <= todayEnd;
    });
    
    // Zbroji ukupnu količinu nikotina za danas
    this.totalTodayNicotine = todayEntries.reduce((sum, entry) => sum + ((entry.kolicina ?? 0) * (entry.nikotinSadrzaj ?? 0)), 0);
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
    
    const nicotineByDay = this.groupNicotineDataByDay(this.nicotineData);
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
          labels,
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
          maintainAspectRatio: false,
          layout: {
            padding: {
              left: 10,
              right: 30
            }
          },
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
                color: '#ffffff',
                maxTicksLimit: 7,
                autoSkip: false,
                padding: 18
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
  
  getLast7Days(): Date[] {
    const result: Date[] = [];
    const today = new Date(); // Use current date
    
    for (let i = 6; i >= 0; i--) {
      const date = new Date(today);
      date.setDate(today.getDate() - i);
      date.setHours(0, 0, 0, 0); // Keep normalizing to start of day for labels
      result.push(date);
    }
    console.log("Last 7 days dates for chart labels:", result.map(d => d.toISOString().split('T')[0]));
    return result;
  }
  
  groupNicotineDataByDay(data: UnosNikotinaExtended[]): Record<string, number> {
    const result: Record<string, number> = {};
    
    if (!data || !Array.isArray(data)) {
      console.error('Invalid data passed to groupNicotineDataByDay:', data);
      return result;
    }
    
    data.forEach(item => {
      if (item.datum) {
        const date = new Date(item.datum);
        const dayStr = date.toISOString().split('T')[0];
        
        if (!result[dayStr]) {
          result[dayStr] = 0;
        }
        
        // Izračunaj ukupnu količinu nikotina (kolicina * nikotinSadrzaj)
        const totalNicotine = (item.kolicina ?? 0) * (item.nikotinSadrzaj ?? 0);
        result[dayStr] += totalNicotine;
      }
    });
    
    return result;
  }
}
