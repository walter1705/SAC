export type RolUsuario = 'ADMINISTRADOR' | 'GESTOR' | 'SOLICITANTE';

export interface LoginRequest {
  nombreUsuario: string;
  contrasena: string;
}

export interface LoginResponse {
  token: string;
  tipo: string;
  nombreUsuario: string;
  rol: RolUsuario;
  expiraEn: number;
}

export interface SesionUsuario extends LoginResponse {}
