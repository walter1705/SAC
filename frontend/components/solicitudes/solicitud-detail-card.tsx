'use client'

import { useSolicitud, useHistorial } from '@/hooks/use-solicitudes'
import { DetailSkeleton } from '@/components/shared/loading-skeleton'
import { SolicitudHeader } from './solicitud-header'
import { SolicitudInfo } from './solicitud-info'
import { HistorialTimeline } from './historial-timeline'
import { AccionesPanel } from './acciones-panel'
import { IaResumenPanel } from './ia-resumen-panel'

interface Props {
  id: number
}

export function SolicitudDetailCard({ id }: Props) {
  const { data: solicitud, isLoading: loadingSolicitud } = useSolicitud(id)
  const { data: historial, isLoading: loadingHistorial } = useHistorial(id)

  if (loadingSolicitud || loadingHistorial) {
    return <DetailSkeleton />
  }

  if (!solicitud) {
    return (
      <div className="text-center py-12">
        <p className="text-muted-foreground">Solicitud no encontrada.</p>
      </div>
    )
  }

  // Infer current assignee from the latest ASIGNACION historial entry
  const currentAssignee = historial
    ?.filter((h) => h.accion === 'ASIGNACION')
    .sort((a, b) => new Date(b.fechaHora).getTime() - new Date(a.fechaHora).getTime())
    [0]?.usuarioResponsable ?? null

  return (
    <div className="space-y-6">
      <SolicitudHeader solicitud={solicitud} />
      <SolicitudInfo solicitud={solicitud} currentAssignee={currentAssignee} />

      <AccionesPanel solicitud={solicitud} />

      <IaResumenPanel solicitudId={id} />

      <div>
        <h2 className="text-lg font-semibold mb-4">Historial</h2>
        <HistorialTimeline historial={historial ?? []} />
      </div>
    </div>
  )
}
