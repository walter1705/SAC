import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  protected readonly cargando = signal(false);
  protected readonly error = signal('');

  protected readonly formulario = this.formBuilder.nonNullable.group({
    nombreUsuario: ['', [Validators.required, Validators.minLength(3)]],
    contrasena: ['', [Validators.required, Validators.minLength(8)]]
  });

  protected iniciarSesion() {
    if (this.formulario.invalid || this.cargando()) {
      this.formulario.markAllAsTouched();
      return;
    }

    this.error.set('');
    this.cargando.set(true);

    this.authService
      .iniciarSesion(this.formulario.getRawValue())
      .pipe(finalize(() => this.cargando.set(false)))
      .subscribe({
        next: () => {
          void this.router.navigate(['/dashboard']);
        },
        error: (error: HttpErrorResponse) => {
          this.error.set(this.obtenerMensajeError(error));
        }
      });
  }

  protected campoInvalido(campo: 'nombreUsuario' | 'contrasena') {
    const control = this.formulario.controls[campo];
    return control.invalid && (control.dirty || control.touched);
  }

  private obtenerMensajeError(error: HttpErrorResponse) {
    if (error.status === 401) {
      return 'Credenciales invalidas. Verifica tu usuario y contrasena.';
    }

    return error.error?.message ?? 'No se pudo iniciar sesion. Intenta nuevamente.';
  }
}
