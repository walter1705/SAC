import { apiClient } from './client'
import type { SugerirClasificacionRequest, SugerirClasificacionResponse, ResumenSolicitudResponse } from '@/lib/types'

export async function sugerirClasificacion(body: SugerirClasificacionRequest): Promise<SugerirClasificacionResponse> {
  return apiClient<SugerirClasificacionResponse>('/ia/sugerir-clasificacion', {
    method: 'POST',
    body: JSON.stringify(body),
  })
}

export async function getResumenSolicitud(id: number): Promise<ResumenSolicitudResponse> {
  return apiClient<ResumenSolicitudResponse>(`/ia/solicitudes/${id}/resumen`)
}
