import {
  ClipboardList,
  Tag,
  ArrowRight,
  UserPlus,
  CheckCircle2,
  CircleDot,
} from 'lucide-react'
import { formatFecha } from '@/lib/utils/formatters'
import type { HistorialResponse } from '@/lib/types'

const accionIcons: Record<string, React.ElementType> = {
  REGISTRO: ClipboardList,
  CLASIFICACION: Tag,
  CAMBIO_ESTADO: ArrowRight,
  ASIGNACION: UserPlus,
  CIERRE: CheckCircle2,
}

const accionLabels: Record<string, string> = {
  REGISTRO: 'Solicitud registrada',
  CLASIFICACION: 'Solicitud clasificada',
  CAMBIO_ESTADO: 'Cambio de estado',
  ASIGNACION: 'Responsable asignado',
  CIERRE: 'Solicitud cerrada',
}

interface Props {
  historial: HistorialResponse[]
}

export function HistorialTimeline({ historial }: Props) {
  if (historial.length === 0) {
    return (
      <p className="text-sm text-muted-foreground py-4">
        Sin registros en el historial.
      </p>
    )
  }

  // Sort ASC by fechaHora
  const sorted = [...historial].sort(
    (a, b) => new Date(a.fechaHora).getTime() - new Date(b.fechaHora).getTime()
  )

  return (
    <div className="relative space-y-0">
      {/* Vertical line */}
      <div className="absolute left-4 top-0 bottom-0 w-px bg-border" />

      {sorted.map((entry) => {
        const Icon = accionIcons[entry.accion] ?? CircleDot
        const label = accionLabels[entry.accion] ?? entry.accion

        return (
          <div key={entry.id} className="relative flex gap-4 pb-6 last:pb-0">
            {/* Icon circle */}
            <div className="relative z-10 flex h-8 w-8 shrink-0 items-center justify-center rounded-full border bg-card">
              <Icon className="h-4 w-4 text-muted-foreground" />
            </div>

            {/* Content */}
            <div className="flex-1 pt-0.5">
              <div className="flex items-center gap-2 flex-wrap">
                <span className="font-medium text-sm">{label}</span>
                <span className="text-xs text-muted-foreground">
                  por {entry.usuarioResponsable}
                </span>
              </div>
              {entry.observaciones && (
                <p className="text-sm text-muted-foreground mt-1">
                  {entry.observaciones}
                </p>
              )}
              <time className="text-xs text-muted-foreground mt-1 block">
                {formatFecha(entry.fechaHora)}
              </time>
            </div>
          </div>
        )
      })}
    </div>
  )
}
