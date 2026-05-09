import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';
import { API_BASE_URL } from './api.config';
import { LoginRequest, LoginResponse, SesionUsuario } from './auth.types';

const STORAGE_KEY = 'sac.sesion';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly sesionSignal = signal<SesionUsuario | null>(this.leerSesionInicial());

  readonly sesion = computed(() => this.sesionSignal());
  readonly autenticado = computed(() => this.sesionSignal() !== null);

  constructor(private readonly http: HttpClient) {}

  iniciarSesion(payload: LoginRequest) {
    return this.http
      .post<LoginResponse>(`${API_BASE_URL}/api/v1/usuarios/login`, payload)
      .pipe(tap((sesion) => this.guardarSesion(sesion)));
  }

  cerrarSesion() {
    localStorage.removeItem(STORAGE_KEY);
    this.sesionSignal.set(null);
  }

  private guardarSesion(sesion: SesionUsuario) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(sesion));
    this.sesionSignal.set(sesion);
  }

  private leerSesionInicial(): SesionUsuario | null {
    const sesionGuardada = localStorage.getItem(STORAGE_KEY);

    if (!sesionGuardada) {
      return null;
    }

    try {
      return JSON.parse(sesionGuardada) as SesionUsuario;
    } catch {
      localStorage.removeItem(STORAGE_KEY);
      return null;
    }
  }
}
