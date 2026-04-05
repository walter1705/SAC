'use client'

import { useState } from 'react'
import { Sparkles, Check, X, Loader2, AlertTriangle } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { useSugerirClasificacion } from '@/hooks/use-ia'
import { TIPO_LABELS, PRIORIDAD_LABELS } from '@/lib/utils/constants'
import { formatConfianza } from '@/lib/utils/formatters'
import type { TipoSolicitud, Prioridad } from '@/lib/types'

interface Props {
  descripcion: string
  onApply: (tipo: TipoSolicitud, prioridad: Prioridad) => void
}

export function IaSuggestionPanel({ descripcion, onApply }: Props) {
  const [dismissed, setDismissed] = useState(false)
  const mutation = useSugerirClasificacion()

  const handleSuggest = () => {
    setDismissed(false)
    mutation.mutate({ descripcion })
  }

  // Confianza color: green ≥70%, amber 40-69%, rose <40%
  function confianzaColor(confianza: number): string {
    if (confianza >= 0.7) return 'text-emerald-500'
    if (confianza >= 0.4) return 'text-amber-500'
    return 'text-rose-500'
  }

  const is503 =
    mutation.isError &&
    mutation.error instanceof Error &&
    'status' in mutation.error &&
    (mutation.error as Error & { status?: number }).status === 503

  return (
    <div className="space-y-3">
      <Button
        type="button"
        variant="outline"
        size="sm"
        onClick={handleSuggest}
        disabled={mutation.isPending || !descripcion || descripcion.length < 10}
      >
        {mutation.isPending ? (
          <Loader2 className="mr-2 h-4 w-4 animate-spin" />
        ) : (
          <Sparkles className="mr-2 h-4 w-4" />
        )}
        Sugerir con IA
      </Button>

      {/* 503 — IA not available */}
      {is503 && (
        <div className="flex items-center gap-2 text-sm text-muted-foreground">
          <AlertTriangle className="h-4 w-4" />
          IA no disponible en este momento
        </div>
      )}

      {/* Non-503 error */}
      {mutation.isError && !is503 && (
        <div className="flex items-center gap-2 text-sm text-destructive">
          <AlertTriangle className="h-4 w-4" />
          Error al obtener sugerencia
        </div>
      )}

      {/* Suggestion result */}
      {mutation.isSuccess && !dismissed && (
        <Card className="border-primary/20 bg-primary/5">
          <CardContent className="pt-4 space-y-3">
            <div className="flex items-center justify-between">
              <span className="text-sm font-medium">Sugerencia de IA</span>
              <span className={`text-sm font-semibold ${confianzaColor(mutation.data.confianza)}`}>
                {formatConfianza(mutation.data.confianza)} de confianza
              </span>
            </div>

            <div className="flex gap-2">
              <Badge variant="outline">{TIPO_LABELS[mutation.data.tipoSugerido]}</Badge>
              <Badge variant="outline">{PRIORIDAD_LABELS[mutation.data.prioridadSugerida]}</Badge>
            </div>

            <p className="text-sm text-muted-foreground">{mutation.data.justificacion}</p>

            <div className="flex gap-2">
              <Button
                type="button"
                size="sm"
                onClick={() => {
                  onApply(mutation.data.tipoSugerido, mutation.data.prioridadSugerida)
                  setDismissed(true)
                }}
              >
                <Check className="mr-1 h-3 w-3" />
                Aplicar sugerencia
              </Button>
              <Button
                type="button"
                variant="ghost"
                size="sm"
                onClick={() => setDismissed(true)}
              >
                <X className="mr-1 h-3 w-3" />
                Ignorar
              </Button>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  )
}
