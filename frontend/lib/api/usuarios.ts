import { apiClient } from './client'
import type { UsuarioResponse, CrearUsuarioRequest, CambiarEstadoUsuarioRequest } from '@/lib/types'

export async function getUsuarios(): Promise<UsuarioResponse[]> {
  return apiClient<UsuarioResponse[]>('/usuarios')
}

export async function crearUsuario(body: CrearUsuarioRequest): Promise<UsuarioResponse> {
  return apiClient<UsuarioResponse>('/usuarios', {
    method: 'POST',
    body: JSON.stringify(body),
  })
}

export async function cambiarEstadoUsuario(
  id: number,
  body: CambiarEstadoUsuarioRequest
): Promise<UsuarioResponse> {
  return apiClient<UsuarioResponse>(`/usuarios/${id}/estado`, {
    method: 'PATCH',
    body: JSON.stringify(body),
  })
}
