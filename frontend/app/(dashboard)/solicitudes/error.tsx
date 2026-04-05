'use client'

import { Button } from '@/components/ui/button'
import { AlertTriangle } from 'lucide-react'

export default function SolicitudesError({
  reset,
}: {
  error: Error & { digest?: string }
  reset: () => void
}) {
  return (
    <div className="flex flex-col items-center justify-center py-12 text-center">
      <AlertTriangle className="h-12 w-12 text-destructive mb-4" />
      <h2 className="text-lg font-semibold">Algo salió mal</h2>
      <p className="text-sm text-muted-foreground mt-1 max-w-sm">
        Ocurrió un error al cargar las solicitudes. Intentá de nuevo.
      </p>
      <Button variant="outline" className="mt-4" onClick={reset}>
        Reintentar
      </Button>
    </div>
  )
}
