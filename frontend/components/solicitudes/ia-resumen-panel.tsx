'use client'

import { useState } from 'react'
import { Sparkles, Loader2, AlertTriangle, ChevronDown, ChevronUp } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Skeleton } from '@/components/ui/skeleton'
import { useResumenSolicitud } from '@/hooks/use-ia'
import { formatFecha } from '@/lib/utils/formatters'

interface Props {
  solicitudId: number
}

export function IaResumenPanel({ solicitudId }: Props) {
  const [expanded, setExpanded] = useState(true)
  const { data, isLoading, isError, error, refetch, isFetched } = useResumenSolicitud(solicitudId)

  const is503 =
    isError &&
    error instanceof Error &&
    'status' in error &&
    (error as Error & { status?: number }).status === 503

  const handleClick = () => {
    if (!isFetched) {
      refetch()
    } else {
      setExpanded(!expanded)
    }
  }

  return (
    <div className="space-y-3">
      <Button
        variant="outline"
        size="sm"
        onClick={handleClick}
        disabled={isLoading}
      >
        {isLoading ? (
          <Loader2 className="mr-2 h-4 w-4 animate-spin" />
        ) : (
          <Sparkles className="mr-2 h-4 w-4" />
        )}
        {isFetched ? (expanded ? 'Ocultar resumen IA' : 'Mostrar resumen IA') : 'Ver resumen IA'}
        {isFetched && (expanded ? <ChevronUp className="ml-1 h-3 w-3" /> : <ChevronDown className="ml-1 h-3 w-3" />)}
      </Button>

      {isLoading && (
        <Card>
          <CardContent className="pt-4 space-y-3">
            <Skeleton className="h-4 w-1/3" />
            <Skeleton className="h-20 w-full" />
          </CardContent>
        </Card>
      )}

      {is503 && (
        <Card className="border-amber-500/20">
          <CardContent className="pt-4">
            <div className="flex items-center gap-2 text-sm text-muted-foreground">
              <AlertTriangle className="h-4 w-4 text-amber-500" />
              El resumen no está disponible en este momento. Intentá más tarde.
            </div>
          </CardContent>
        </Card>
      )}

      {isError && !is503 && (
        <Card className="border-destructive/20">
          <CardContent className="pt-4">
            <div className="flex items-center gap-2 text-sm text-destructive">
              <AlertTriangle className="h-4 w-4" />
              Error al obtener el resumen
            </div>
          </CardContent>
        </Card>
      )}

      {data && expanded && (
        <Card className="border-primary/20">
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium flex items-center gap-2">
              <Sparkles className="h-4 w-4 text-primary" />
              Resumen generado por IA
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-2">
            <p className="text-sm whitespace-pre-wrap">{data.resumen}</p>
            <p className="text-xs text-muted-foreground">
              Generado el {formatFecha(data.generadoEn)}
            </p>
          </CardContent>
        </Card>
      )}
    </div>
  )
}
