import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Separator } from '@/components/ui/separator'
import { User, FileText, CheckCircle2 } from 'lucide-react'
import { TIPO_LABELS, PRIORIDAD_LABELS, CANAL_LABELS } from '@/lib/utils/constants'
import { formatFecha } from '@/lib/utils/formatters'
import type { SolicitudResponse } from '@/lib/types'

interface Props {
  solicitud: SolicitudResponse
  currentAssignee: string | null
}

export function SolicitudInfo({ solicitud, currentAssignee }: Props) {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      {/* Student Info Card */}
      <Card>
        <CardHeader className="pb-3">
          <CardTitle className="text-sm font-medium flex items-center gap-2">
            <User className="h-4 w-4" />
            Datos del estudiante
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-2 text-sm">
          <InfoRow label="Nombre" value={solicitud.estudianteNombre} />
          <InfoRow label="Correo" value={solicitud.estudianteCorreo} />
          <InfoRow label="Teléfono" value={solicitud.estudianteTelefono} />
          <InfoRow label="Identificación" value={solicitud.estudianteIdentificacion} />
        </CardContent>
      </Card>

      {/* Request Info Card */}
      <Card>
        <CardHeader className="pb-3">
          <CardTitle className="text-sm font-medium flex items-center gap-2">
            <FileText className="h-4 w-4" />
            Datos de la solicitud
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-2 text-sm">
          <InfoRow label="Canal" value={CANAL_LABELS[solicitud.canalOrigen]} />
          <InfoRow label="Fecha" value={formatFecha(solicitud.fechaHoraRegistro)} />
          <InfoRow
            label="Tipo"
            value={solicitud.tipo ? TIPO_LABELS[solicitud.tipo] : 'Pendiente de clasificación'}
            muted={!solicitud.tipo}
          />
          <InfoRow
            label="Prioridad"
            value={solicitud.prioridad ? PRIORIDAD_LABELS[solicitud.prioridad] : 'Pendiente de clasificación'}
            muted={!solicitud.prioridad}
          />
          <InfoRow
            label="Nota de clasificación"
            value={solicitud.notaClasificacion ?? '—'}
            muted={!solicitud.notaClasificacion}
          />
          {currentAssignee && (
            <InfoRow label="Responsable actual" value={currentAssignee} />
          )}
        </CardContent>
      </Card>

      {/* Description Card — full width */}
      <Card className="md:col-span-2">
        <CardHeader className="pb-3">
          <CardTitle className="text-sm font-medium">Descripción</CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-sm whitespace-pre-wrap">{solicitud.descripcion}</p>
        </CardContent>
      </Card>

      {/* Resolution Card — only when CERRADA */}
      {solicitud.estado === 'CERRADA' && (
        <Card className="md:col-span-2 border-[hsl(var(--estado-cerrada))]/30">
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium flex items-center gap-2">
              <CheckCircle2 className="h-4 w-4 text-[hsl(var(--estado-cerrada))]" />
              Resolución
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-2 text-sm">
            <p className="whitespace-pre-wrap">{solicitud.resolucion}</p>
            {solicitud.notasCierre && (
              <>
                <Separator />
                <div>
                  <span className="text-muted-foreground">Notas de cierre: </span>
                  {solicitud.notasCierre}
                </div>
              </>
            )}
            {!solicitud.notasCierre && (
              <>
                <Separator />
                <span className="text-muted-foreground">Sin notas de cierre</span>
              </>
            )}
          </CardContent>
        </Card>
      )}
    </div>
  )
}

function InfoRow({ label, value, muted }: { label: string; value: string; muted?: boolean }) {
  return (
    <div className="flex justify-between gap-2">
      <span className="text-muted-foreground">{label}</span>
      <span className={muted ? 'text-muted-foreground italic' : 'font-medium text-right'}>{value}</span>
    </div>
  )
}
