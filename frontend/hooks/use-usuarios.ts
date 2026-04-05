'use client'

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { toast } from 'sonner'
import { getUsuarios, crearUsuario, cambiarEstadoUsuario } from '@/lib/api/usuarios'
import { ApiError } from '@/lib/api/client'

export function useUsuarios() {
  return useQuery({
    queryKey: ['usuarios'],
    queryFn: getUsuarios,
  })
}

export function useCrearUsuario() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: crearUsuario,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['usuarios'] })
      toast.success('Usuario creado correctamente')
    },
    onError: (error) => {
      // Don't toast for 409 — form handles those inline
      if (error instanceof ApiError && error.status === 409) return
      toast.error(error instanceof Error ? error.message : 'Error al crear usuario')
    },
  })
}

export function useCambiarEstadoUsuario() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ id, body }: { id: number; body: { activo: boolean } }) =>
      cambiarEstadoUsuario(id, body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['usuarios'] })
      toast.success('Estado del usuario actualizado')
    },
    onError: (error) => {
      toast.error(error instanceof Error ? error.message : 'Error al cambiar estado')
    },
  })
}
