'use client'

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { useRouter } from 'next/navigation'
import { toast } from 'sonner'
import type { SolicitudFilterParams } from '@/lib/types'
import {
  getSolicitudes,
  getSolicitud,
  getHistorial,
  crearSolicitud,
  clasificarSolicitud,
  cambiarEstado,
  asignarResponsable,
  cerrarSolicitud,
} from '@/lib/api/solicitudes'

export function useSolicitudes(filters: SolicitudFilterParams) {
  return useQuery({
    queryKey: ['solicitudes', filters],
    queryFn: () => getSolicitudes(filters),
  })
}

export function useSolicitud(id: number) {
  return useQuery({
    queryKey: ['solicitudes', id],
    queryFn: () => getSolicitud(id),
    staleTime: 60_000,
  })
}

export function useHistorial(id: number) {
  return useQuery({
    queryKey: ['solicitudes', id, 'historial'],
    queryFn: () => getHistorial(id),
    staleTime: 0,
  })
}

export function useCrearSolicitud() {
  const queryClient = useQueryClient()
  const router = useRouter()

  return useMutation({
    mutationFn: crearSolicitud,
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['solicitudes'] })
      toast.success('Solicitud creada exitosamente')
      router.push(`/solicitudes/${data.id}`)
    },
    onError: (error) => {
      // Don't toast for 400 errors — form handles those
      if (error instanceof Error && 'status' in error && (error as { status?: number }).status === 400) return
      toast.error(error instanceof Error ? error.message : 'Error al crear solicitud')
    },
  })
}

export function useClasificar(id: number) {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (body: Parameters<typeof clasificarSolicitud>[1]) => clasificarSolicitud(id, body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['solicitudes', id] })
      queryClient.invalidateQueries({ queryKey: ['solicitudes', id, 'historial'] })
      queryClient.invalidateQueries({ queryKey: ['solicitudes'] })
      toast.success('Solicitud clasificada correctamente')
    },
    onError: (error) => {
      toast.error(error instanceof Error ? error.message : 'Error al clasificar')
    },
  })
}

export function useCambiarEstado(id: number) {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (body: Parameters<typeof cambiarEstado>[1]) => cambiarEstado(id, body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['solicitudes', id] })
      queryClient.invalidateQueries({ queryKey: ['solicitudes', id, 'historial'] })
      queryClient.invalidateQueries({ queryKey: ['solicitudes'] })
      toast.success('Estado actualizado correctamente')
    },
    onError: (error) => {
      toast.error(error instanceof Error ? error.message : 'Error al cambiar estado')
    },
  })
}

export function useAsignar(id: number) {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (body: Parameters<typeof asignarResponsable>[1]) => asignarResponsable(id, body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['solicitudes', id] })
      queryClient.invalidateQueries({ queryKey: ['solicitudes', id, 'historial'] })
      queryClient.invalidateQueries({ queryKey: ['solicitudes'] })
      toast.success('Responsable asignado correctamente')
    },
    onError: (error) => {
      toast.error(error instanceof Error ? error.message : 'Error al asignar responsable')
    },
  })
}

export function useCerrar(id: number) {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (body: Parameters<typeof cerrarSolicitud>[1]) => cerrarSolicitud(id, body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['solicitudes', id] })
      queryClient.invalidateQueries({ queryKey: ['solicitudes', id, 'historial'] })
      queryClient.invalidateQueries({ queryKey: ['solicitudes'] })
      toast.success('Solicitud cerrada correctamente')
    },
    onError: (error) => {
      toast.error(error instanceof Error ? error.message : 'Error al cerrar solicitud')
    },
  })
}
