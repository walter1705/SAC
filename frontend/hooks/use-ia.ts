'use client'

import { useQuery, useMutation } from '@tanstack/react-query'
import { sugerirClasificacion, getResumenSolicitud } from '@/lib/api/ia'

export function useSugerirClasificacion() {
  return useMutation({
    mutationFn: sugerirClasificacion,
    // No toast on error — handled inline in the component
  })
}

export function useResumenSolicitud(id: number) {
  return useQuery({
    queryKey: ['ia', 'resumen', id],
    queryFn: () => getResumenSolicitud(id),
    enabled: false, // Lazy — only fetches when refetch() is called
    retry: false,
    staleTime: Infinity,
  })
}
