import { Component, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  protected readonly sesion = this.authService.sesion;
  protected readonly nombreUsuario = computed(() => this.sesion()?.nombreUsuario ?? '');
  protected readonly rol = computed(() => this.sesion()?.rol ?? '');

  protected cerrarSesion() {
    this.authService.cerrarSesion();
    void this.router.navigate(['/login']);
  }
}
