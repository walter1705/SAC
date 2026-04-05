// Enums
export type EstadoSolicitud = 'REGISTRADA' | 'CLASIFICADA' | 'EN_ATENCION' | 'ATENDIDA' | 'CERRADA'
export type TipoSolicitud = 'REGISTRO' | 'HOMOLOGACION' | 'CANCELACION' | 'CUPOS' | 'CONSULTA'
export type Prioridad = 'ALTA' | 'MEDIA' | 'BAJA'
export type RolUsuario = 'ADMINISTRADOR' | 'GESTOR' | 'SOLICITANTE'
export type CanalOrigen = 'CSU' | 'EMAIL' | 'WEB' | 'SAC' | 'TELEFONICO' | 'PRESENCIAL'

// Response types
export interface SolicitudResponse {
  id: number
  estudianteNombre: string
  estudianteCorreo: string
  estudianteTelefono: string
  estudianteIdentificacion: string
  asunto: string
  descripcion: string
  canalOrigen: CanalOrigen
  fechaHoraRegistro: string
  tipo: TipoSolicitud | null
  prioridad: Prioridad | null
  notaClasificacion: string | null
  estado: EstadoSolicitud
  resolucion: string | null
  notasCierre: string | null
}

// Raw pagination (with ñ from backend)
export interface SolicitudesPaginadasRaw {
  content: SolicitudResponse[]
  pagina: number
  'tamaño': number
  totalElementos: number
  totalPaginas: number
}

// Normalized pagination (frontend use)
export interface SolicitudesPaginadas {
  content: SolicitudResponse[]
  pagina: number
  tamanio: number
  totalElementos: number
  totalPaginas: number
}

export interface HistorialResponse {
  id: number
  fechaHora: string
  accion: string
  usuarioResponsable: string
  observaciones: string
}

export interface AsignacionResponse {
  id: number
  solicitudId: number
  usuarioId: number
  fechaAsignacion: string
  activa: boolean
}

export interface UsuarioResponse {
  id: number
  nombreCompleto: string
  nombreUsuario: string
  email: string
  rol: RolUsuario
  activo: boolean
}

export interface LoginResponse {
  token: string
  tipo: string
  nombreUsuario: string
  rol: RolUsuario
  expiraEn: number
}

export interface SugerirClasificacionResponse {
  tipoSugerido: TipoSolicitud
  prioridadSugerida: Prioridad
  justificacion: string
  confianza: number
}

export interface ResumenSolicitudResponse {
  idSolicitud: number
  resumen: string
  generadoEn: string
}

// Request types
export interface LoginRequest {
  nombreUsuario: string
  contrasena: string
}

export interface CrearSolicitudRequest {
  estudianteNombre: string
  estudianteCorreo: string
  estudianteTelefono: string
  estudianteIdentificacion: string
  asunto: string
  descripcion: string
  canalOrigen: CanalOrigen
}

export interface ClasificarSolicitudRequest {
  tipo: TipoSolicitud
  prioridad: Prioridad
  notaClasificacion: string
}

export interface CambiarEstadoRequest {
  nuevoEstado: EstadoSolicitud
  nota?: string
}

export interface AsignarResponsableRequest {
  responsableId: number
  notaAsignacion?: string
}

export interface CerrarSolicitudRequest {
  resolucion: string
  notasCierre?: string
}

export interface CrearUsuarioRequest {
  nombreCompleto: string
  nombreUsuario: string
  contrasena: string
  email: string
  rol: RolUsuario
}

export interface CambiarEstadoUsuarioRequest {
  activo: boolean
}

export interface SugerirClasificacionRequest {
  descripcion: string
}

// API error shape
export interface ApiError {
  status: number
  message: string
  errors?: Record<string, string>
}

// Decoded JWT payload
export interface TokenPayload {
  sub: string      // nombreUsuario
  rol: RolUsuario
  userId: number
  iat: number
  exp: number
}

// Filter params for solicitudes list
export interface SolicitudFilterParams {
  estado?: EstadoSolicitud[]
  tipo?: TipoSolicitud[]
  prioridad?: Prioridad[]
  responsable?: number
  page?: number
  size?: number
  sort?: string
}
