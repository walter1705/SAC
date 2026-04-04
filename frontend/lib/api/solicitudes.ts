import { apiClient } from './client'
import type {
  SolicitudResponse,
  SolicitudesPaginadas,
  SolicitudesPaginadasRaw,
  SolicitudFilterParams,
  HistorialResponse,
  AsignacionResponse,
  CrearSolicitudRequest,
  ClasificarSolicitudRequest,
  CambiarEstadoRequest,
  AsignarResponsableRequest,
  CerrarSolicitudRequest,
} from '@/lib/types'

function normalizePagination(raw: SolicitudesPaginadasRaw): SolicitudesPaginadas {
  return {
    content: raw.content,
    pagina: raw.pagina,
    tamanio: raw['tamaño'],
    totalElementos: raw.totalElementos,
    totalPaginas: raw.totalPaginas,
  }
}

export async function getSolicitudes(params: SolicitudFilterParams = {}): Promise<SolicitudesPaginadas> {
  const searchParams = new URLSearchParams()

  if (params.estado) {
    params.estado.forEach(e => searchParams.append('estado', e))
  }
  if (params.tipo) {
    params.tipo.forEach(t => searchParams.append('tipo', t))
  }
  if (params.prioridad) {
    params.prioridad.forEach(p => searchParams.append('prioridad', p))
  }
  if (params.responsable) {
    searchParams.set('responsable', String(params.responsable))
  }
  searchParams.set('page', String(params.page ?? 0))
  searchParams.set('size', String(params.size ?? 20))
  searchParams.set('sort', params.sort ?? 'fechaHoraRegistro,desc')

  const query = searchParams.toString()
  const raw = await apiClient<SolicitudesPaginadasRaw>(`/solicitudes?${query}`)
  return normalizePagination(raw)
}

export async function getSolicitud(id: number): Promise<SolicitudResponse> {
  return apiClient<SolicitudResponse>(`/solicitudes/${id}`)
}

export async function getHistorial(id: number): Promise<HistorialResponse[]> {
  return apiClient<HistorialResponse[]>(`/solicitudes/${id}/historial`)
}

export async function crearSolicitud(body: CrearSolicitudRequest): Promise<SolicitudResponse> {
  return apiClient<SolicitudResponse>('/solicitudes', {
    method: 'POST',
    body: JSON.stringify(body),
  })
}

export async function clasificarSolicitud(id: number, body: ClasificarSolicitudRequest): Promise<SolicitudResponse> {
  return apiClient<SolicitudResponse>(`/solicitudes/${id}/clasificar`, {
    method: 'PATCH',
    body: JSON.stringify(body),
  })
}

export async function cambiarEstado(id: number, body: CambiarEstadoRequest): Promise<SolicitudResponse> {
  return apiClient<SolicitudResponse>(`/solicitudes/${id}/estado`, {
    method: 'PATCH',
    body: JSON.stringify(body),
  })
}

export async function asignarResponsable(id: number, body: AsignarResponsableRequest): Promise<AsignacionResponse> {
  return apiClient<AsignacionResponse>(`/solicitudes/${id}/asignar`, {
    method: 'POST',
    body: JSON.stringify(body),
  })
}

export async function cerrarSolicitud(id: number, body: CerrarSolicitudRequest): Promise<SolicitudResponse> {
  return apiClient<SolicitudResponse>(`/solicitudes/${id}/cerrar`, {
    method: 'PATCH',
    body: JSON.stringify(body),
  })
}
