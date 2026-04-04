import Link from 'next/link'
import { ArrowLeft } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { EstadoBadge } from '@/components/shared/estado-badge'
import { PrioridadBadge } from '@/components/shared/prioridad-badge'
import type { SolicitudResponse } from '@/lib/types'

interface Props {
  solicitud: SolicitudResponse
}

export function SolicitudHeader({ solicitud }: Props) {
  return (
    <div className="space-y-2">
      <Button variant="ghost" size="sm" asChild>
        <Link href="/solicitudes">
          <ArrowLeft className="h-4 w-4 mr-1" />
          Volver a solicitudes
        </Link>
      </Button>
      <div className="flex items-start justify-between gap-4">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="text-sm font-mono text-muted-foreground">#{solicitud.id}</span>
            <EstadoBadge estado={solicitud.estado} />
            <PrioridadBadge prioridad={solicitud.prioridad} />
          </div>
          <h1 className="text-2xl font-bold">{solicitud.asunto}</h1>
        </div>
      </div>
    </div>
  )
}
