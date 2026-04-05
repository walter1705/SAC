import type { EstadoSolicitud, TipoSolicitud, Prioridad, CanalOrigen, RolUsuario } from '@/lib/types'

export const ESTADO_LABELS: Record<EstadoSolicitud, string> = {
  REGISTRADA: 'Registrada',
  CLASIFICADA: 'Clasificada',
  EN_ATENCION: 'En atención',
  ATENDIDA: 'Atendida',
  CERRADA: 'Cerrada',
}

export const TIPO_LABELS: Record<TipoSolicitud, string> = {
  REGISTRO: 'Registro',
  HOMOLOGACION: 'Homologación',
  CANCELACION: 'Cancelación',
  CUPOS: 'Cupos',
  CONSULTA: 'Consulta',
}

export const PRIORIDAD_LABELS: Record<Prioridad, string> = {
  ALTA: 'Alta',
  MEDIA: 'Media',
  BAJA: 'Baja',
}

export const CANAL_LABELS: Record<CanalOrigen, string> = {
  CSU: 'CSU',
  EMAIL: 'Correo electrónico',
  WEB: 'Web',
  SAC: 'SAC',
  TELEFONICO: 'Telefónico',
  PRESENCIAL: 'Presencial',
}

export const ROL_LABELS: Record<RolUsuario, string> = {
  ADMINISTRADOR: 'Administrador',
  GESTOR: 'Gestor',
  SOLICITANTE: 'Solicitante',
}

// State machine: what actions are available at each state for GESTOR
export interface AccionSolicitud {
  key: string
  label: string
  type: 'modal' | 'button'
}

export const ACCIONES_POR_ESTADO: Record<EstadoSolicitud, AccionSolicitud[]> = {
  REGISTRADA: [
    { key: 'clasificar', label: 'Clasificar', type: 'modal' },
    { key: 'asignar', label: 'Asignar responsable', type: 'modal' },
  ],
  CLASIFICADA: [
    { key: 'iniciar', label: 'Iniciar atención', type: 'button' },
    { key: 'asignar', label: 'Asignar responsable', type: 'modal' },
  ],
  EN_ATENCION: [
    { key: 'atender', label: 'Marcar como atendida', type: 'button' },
    { key: 'asignar', label: 'Asignar responsable', type: 'modal' },
  ],
  ATENDIDA: [
    { key: 'cerrar', label: 'Cerrar solicitud', type: 'modal' },
  ],
  CERRADA: [],
}

// Nav links per role
export interface NavLink {
  href: string
  label: string
  icon: string  // lucide icon name
  roles: RolUsuario[]
}

export const NAV_LINKS: NavLink[] = [
  { href: '/solicitudes', label: 'Solicitudes', icon: 'FileText', roles: ['ADMINISTRADOR', 'GESTOR', 'SOLICITANTE'] },
  { href: '/usuarios', label: 'Usuarios', icon: 'Users', roles: ['ADMINISTRADOR'] },
]
