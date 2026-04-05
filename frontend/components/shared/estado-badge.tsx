import { Badge } from '@/components/ui/badge'
import type { EstadoSolicitud } from '@/lib/types'
import { ESTADO_LABELS } from '@/lib/utils/constants'

const estadoStyles: Record<EstadoSolicitud, string> = {
  REGISTRADA: 'bg-[hsl(var(--estado-registrada))] text-white',
  CLASIFICADA: 'bg-[hsl(var(--estado-clasificada))] text-white',
  EN_ATENCION: 'bg-[hsl(var(--estado-en-atencion))] text-white',
  ATENDIDA: 'bg-[hsl(var(--estado-atendida))] text-white',
  CERRADA: 'bg-[hsl(var(--estado-cerrada))] text-white',
}

export function EstadoBadge({ estado }: { estado: EstadoSolicitud }) {
  return (
    <Badge className={estadoStyles[estado]}>
      {ESTADO_LABELS[estado]}
    </Badge>
  )
}
