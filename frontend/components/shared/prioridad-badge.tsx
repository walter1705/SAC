import { Badge } from '@/components/ui/badge'
import type { Prioridad } from '@/lib/types'
import { PRIORIDAD_LABELS } from '@/lib/utils/constants'

const prioridadStyles: Record<Prioridad, string> = {
  ALTA: 'bg-[hsl(var(--priority-alta))] text-white',
  MEDIA: 'bg-[hsl(var(--priority-media))] text-white',
  BAJA: 'bg-[hsl(var(--priority-baja))] text-white',
}

export function PrioridadBadge({ prioridad }: { prioridad: Prioridad | null }) {
  if (!prioridad) {
    return (
      <Badge variant="outline" className="text-muted-foreground">
        Sin clasificar
      </Badge>
    )
  }

  return (
    <Badge className={prioridadStyles[prioridad]}>
      {PRIORIDAD_LABELS[prioridad]}
    </Badge>
  )
}
