import { Component } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterModule],
  template: `
    <div class="min-h-screen bg-gray-900 text-white">
      <!-- Header -->
      <header
        class="px-6 py-4 text-xl font-bold"
        style="background-color: #73EC8B; color: #1f1f1f;"
      >
        <a
          routerLink="/"
          class="hover:underline"
          style="color: inherit; text-decoration: none;"
        >
          NicotiNO
        </a>
      </header>

      <main class="p-6 grid grid-cols-1 md:grid-cols-[56px_1fr_320px] gap-6">
        <!-- Sidebar Tabs -->
        <nav class="hidden md:flex flex-col space-y-4">
          <a
            class="w-12 h-12 flex items-center justify-center rounded-lg bg-gray-800 hover:bg-green-400 transition"
            title="Unos nikotina"
            routerLink="/nicotine-intake"
          >
            <span class="material-icons text-green-300 text-3xl"
              >add_circle</span
            >
          </a>
          <a
            class="w-12 h-12 flex items-center justify-center rounded-lg bg-gray-800 hover:bg-blue-400 transition"
            title="Kupnja nikotina"
            routerLink="/nicotine-purchase"
          >
            <span class="material-icons text-blue-300 text-3xl"
              >shopping_cart</span
            >
          </a>
          <a
            class="w-12 h-12 flex items-center justify-center rounded-lg bg-gray-800 hover:bg-yellow-400 transition"
            title="Statistika"
            routerLink="/statistics"
          >
            <span class="material-icons text-yellow-300 text-3xl"
              >bar_chart</span
            >
          </a>
          <a
            class="w-12 h-12 flex items-center justify-center rounded-lg bg-gray-800 hover:bg-purple-400 transition"
            title="Postignuća"
            routerLink="/achievements"
          >
            <span class="material-icons text-purple-300 text-3xl"
              >emoji_events</span
            >
          </a>
        </nav>

        <!-- Page Content -->
        <section class="md:col-span-2">
          <router-outlet></router-outlet>
        </section>
      </main>
    </div>
  `,
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'frontend';
}
