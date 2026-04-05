'use client'

import { useState } from 'react'
import { useMutation } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { motion, AnimatePresence } from 'framer-motion'
import {
  Brain,
  FileSearch,
  ArrowLeft,
  Download,
  Copy,
  Loader2,
  Sparkles,
} from 'lucide-react'
import { toast } from 'sonner'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Separator } from '@/components/ui/separator'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { sugerirClasificacion, getResumenSolicitud } from '@/lib/api/ia'
import type { SugerirClasificacionResponse, ResumenSolicitudResponse } from '@/lib/types'
import { TIPO_LABELS, PRIORIDAD_LABELS } from '@/lib/utils/constants'
import { formatFecha, formatConfianza } from '@/lib/utils/formatters'

// ---------------------------------------------------------------------------
// State shape
// ---------------------------------------------------------------------------

type Mode = 'clasificacion' | 'resumen'

type SheetState =
  | { step: 'select' }
  | { step: 'form'; mode: Mode }
  | { step: 'response'; mode: 'clasificacion'; data: SugerirClasificacionResponse }
  | { step: 'response'; mode: 'resumen'; data: ResumenSolicitudResponse }

// ---------------------------------------------------------------------------
// Schemas
// ---------------------------------------------------------------------------

const clasificacionSchema = z.object({
  descripcion: z.string().min(10, 'Mínimo 10 caracteres').max(1000, 'Máximo 1000 caracteres'),
})

const resumenSchema = z.object({
  solicitudId: z.number().int().positive('Debe ser un ID positivo'),
})

type ClasificacionForm = z.infer<typeof clasificacionSchema>
type ResumenForm = z.infer<typeof resumenSchema>

// ---------------------------------------------------------------------------
// Download helper
// ---------------------------------------------------------------------------

function downloadTxt(filename: string, content: string) {
  const blob = new Blob([content], { type: 'text/plain;charset=utf-8' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = filename
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
  URL.revokeObjectURL(url)
}

function buildClasificacionTxt(data: SugerirClasificacionResponse): string {
  return [
    'SAC — Sugerencia de Clasificación',
    `Generado: ${new Date().toLocaleString('es-CO')}`,
    '',
    `Tipo sugerido:      ${TIPO_LABELS[data.tipoSugerido]}`,
    `Prioridad sugerida: ${PRIORIDAD_LABELS[data.prioridadSugerida]}`,
    `Confianza:          ${formatConfianza(data.confianza)}`,
    '',
    'Justificación:',
    data.justificacion,
  ].join('\n')
}

function buildResumenTxt(data: ResumenSolicitudResponse): string {
  return [
    `SAC — Resumen de Solicitud #${data.idSolicitud}`,
    `Generado: ${formatFecha(data.generadoEn)}`,
    '',
    data.resumen,
  ].join('\n')
}

// ---------------------------------------------------------------------------
// Step 1 — Mode selector
// ---------------------------------------------------------------------------

const MODES = [
  {
    id: 'clasificacion' as Mode,
    icon: Brain,
    title: 'Sugerir clasificación',
    description: 'Obtiene una sugerencia de tipo y prioridad a partir de la descripción de la solicitud.',
  },
  {
    id: 'resumen' as Mode,
    icon: FileSearch,
    title: 'Resumen de solicitud',
    description: 'Genera un resumen ejecutivo del historial y estado actual de una solicitud.',
  },
]

function ModeSelector({ onSelect }: { onSelect: (mode: Mode) => void }) {
  return (
    <motion.div
      key="select"
      initial={{ opacity: 0, x: -16 }}
      animate={{ opacity: 1, x: 0 }}
      exit={{ opacity: 0, x: 16 }}
      className="flex flex-col gap-4"
    >
      <p className="text-sm text-muted-foreground">
        Seleccioná qué querés hacer con la IA:
      </p>
      {MODES.map((m) => {
        const Icon = m.icon
        return (
          <Card
            key={m.id}
            className="cursor-pointer border-2 transition-colors hover:border-primary hover:bg-primary/5"
            onClick={() => onSelect(m.id)}
          >
            <CardHeader className="pb-2">
              <CardTitle className="flex items-center gap-2 text-base">
                <Icon className="h-5 w-5 text-primary" />
                {m.title}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <CardDescription>{m.description}</CardDescription>
            </CardContent>
          </Card>
        )
      })}
    </motion.div>
  )
}

// ---------------------------------------------------------------------------
// Step 2a — Clasificación form
// ---------------------------------------------------------------------------

function ClasificacionForm({
  onBack,
  onSuccess,
}: {
  onBack: () => void
  onSuccess: (data: SugerirClasificacionResponse) => void
}) {
  const form = useForm<ClasificacionForm>({
    resolver: zodResolver(clasificacionSchema),
    defaultValues: { descripcion: '' },
  })

  const mutation = useMutation({
    mutationFn: sugerirClasificacion,
    retry: false,
    onSuccess,
    onError: (error: Error) => {
      const msg = error.message.includes('503')
        ? 'El servicio de IA no está disponible en este momento.'
        : error.message
      toast.error(msg)
    },
  })

  return (
    <motion.div
      key="form-clasificacion"
      initial={{ opacity: 0, x: 16 }}
      animate={{ opacity: 1, x: 0 }}
      exit={{ opacity: 0, x: -16 }}
      className="flex flex-col gap-4"
    >
      <Button variant="ghost" size="sm" className="w-fit -ml-2" onClick={onBack}>
        <ArrowLeft className="h-4 w-4 mr-1" /> Volver
      </Button>

      <div>
        <h3 className="font-semibold flex items-center gap-2">
          <Brain className="h-4 w-4 text-primary" /> Sugerir clasificación
        </h3>
        <p className="text-sm text-muted-foreground mt-1">
          Pegá la descripción de la solicitud para obtener una sugerencia de tipo y prioridad.
        </p>
      </div>

      <Form {...form}>
        <form
          onSubmit={form.handleSubmit((values) => mutation.mutate(values))}
          className="flex flex-col gap-4"
        >
          <FormField
            control={form.control}
            name="descripcion"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Descripción de la solicitud</FormLabel>
                <FormControl>
                  <Textarea
                    placeholder="El estudiante solicita homologación de la materia Cálculo I cursada en otra institución..."
                    className="min-h-[140px] resize-none"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <Button type="submit" disabled={mutation.isPending} className="w-full">
            {mutation.isPending ? (
              <><Loader2 className="h-4 w-4 mr-2 animate-spin" /> Consultando IA...</>
            ) : (
              <><Sparkles className="h-4 w-4 mr-2" /> Obtener sugerencia</>
            )}
          </Button>
        </form>
      </Form>
    </motion.div>
  )
}

// ---------------------------------------------------------------------------
// Step 2b — Resumen form
// ---------------------------------------------------------------------------

function ResumenForm({
  onBack,
  onSuccess,
}: {
  onBack: () => void
  onSuccess: (data: ResumenSolicitudResponse) => void
}) {
  const form = useForm<ResumenForm>({
    resolver: zodResolver(resumenSchema),
  })

  const mutation = useMutation({
    mutationFn: ({ solicitudId }: ResumenForm) => getResumenSolicitud(solicitudId),
    retry: false,
    onSuccess,
    onError: (error: Error) => {
      const msg = error.message.includes('503')
        ? 'El servicio de IA no está disponible en este momento.'
        : error.message.includes('404')
        ? 'No se encontró una solicitud con ese ID.'
        : error.message
      toast.error(msg)
    },
  })

  return (
    <motion.div
      key="form-resumen"
      initial={{ opacity: 0, x: 16 }}
      animate={{ opacity: 1, x: 0 }}
      exit={{ opacity: 0, x: -16 }}
      className="flex flex-col gap-4"
    >
      <Button variant="ghost" size="sm" className="w-fit -ml-2" onClick={onBack}>
        <ArrowLeft className="h-4 w-4 mr-1" /> Volver
      </Button>

      <div>
        <h3 className="font-semibold flex items-center gap-2">
          <FileSearch className="h-4 w-4 text-primary" /> Resumen de solicitud
        </h3>
        <p className="text-sm text-muted-foreground mt-1">
          Ingresá el ID de la solicitud para generar un resumen ejecutivo.
        </p>
      </div>

      <Form {...form}>
        <form
          onSubmit={form.handleSubmit((values) => mutation.mutate(values))}
          className="flex flex-col gap-4"
        >
          <FormField
            control={form.control}
            name="solicitudId"
            render={({ field }) => (
              <FormItem>
                <FormLabel>ID de solicitud</FormLabel>
                <FormControl>
                  <Input
                    type="number"
                    placeholder="123"
                    min={1}
                    value={field.value || ''}
                    onChange={(e) => field.onChange(e.target.valueAsNumber)}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <Button type="submit" disabled={mutation.isPending} className="w-full">
            {mutation.isPending ? (
              <><Loader2 className="h-4 w-4 mr-2 animate-spin" /> Generando resumen...</>
            ) : (
              <><Sparkles className="h-4 w-4 mr-2" /> Generar resumen</>
            )}
          </Button>
        </form>
      </Form>
    </motion.div>
  )
}

// ---------------------------------------------------------------------------
// Step 3a — Clasificación response
// ---------------------------------------------------------------------------

const PRIORIDAD_COLORS: Record<string, string> = {
  ALTA: 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400',
  MEDIA: 'bg-amber-100 text-amber-700 dark:bg-amber-900/30 dark:text-amber-400',
  BAJA: 'bg-emerald-100 text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-400',
}

function ClasificacionResponse({
  data,
  onReset,
}: {
  data: SugerirClasificacionResponse
  onReset: () => void
}) {
  const handleCopy = () => {
    navigator.clipboard.writeText(buildClasificacionTxt(data))
    toast.success('Copiado al portapapeles')
  }

  return (
    <motion.div
      key="response-clasificacion"
      initial={{ opacity: 0, y: 8 }}
      animate={{ opacity: 1, y: 0 }}
      className="flex flex-col gap-4"
    >
      <div className="flex items-center justify-between">
        <h3 className="font-semibold flex items-center gap-2">
          <Brain className="h-4 w-4 text-primary" /> Sugerencia de clasificación
        </h3>
        <Badge variant="outline" className="text-xs">
          Confianza: {formatConfianza(data.confianza)}
        </Badge>
      </div>

      <Card className="border-primary/20 bg-primary/5">
        <CardContent className="pt-4 flex flex-col gap-3">
          <div className="flex items-center justify-between">
            <span className="text-sm text-muted-foreground">Tipo sugerido</span>
            <Badge variant="secondary">{TIPO_LABELS[data.tipoSugerido]}</Badge>
          </div>
          <div className="flex items-center justify-between">
            <span className="text-sm text-muted-foreground">Prioridad sugerida</span>
            <span className={`text-xs font-medium px-2.5 py-0.5 rounded-full ${PRIORIDAD_COLORS[data.prioridadSugerida]}`}>
              {PRIORIDAD_LABELS[data.prioridadSugerida]}
            </span>
          </div>
        </CardContent>
      </Card>

      <div>
        <p className="text-xs font-medium text-muted-foreground uppercase tracking-wide mb-1">Justificación</p>
        <p className="text-sm leading-relaxed">{data.justificacion}</p>
      </div>

      <Separator />

      <div className="flex gap-2">
        <Button
          variant="outline"
          size="sm"
          className="flex-1"
          onClick={handleCopy}
        >
          <Copy className="h-4 w-4 mr-1" /> Copiar
        </Button>
        <Button
          variant="outline"
          size="sm"
          className="flex-1"
          onClick={() => downloadTxt('clasificacion.txt', buildClasificacionTxt(data))}
        >
          <Download className="h-4 w-4 mr-1" /> Descargar .txt
        </Button>
      </div>

      <Button variant="ghost" size="sm" onClick={onReset}>
        Nueva consulta
      </Button>
    </motion.div>
  )
}

// ---------------------------------------------------------------------------
// Step 3b — Resumen response
// ---------------------------------------------------------------------------

function ResumenResponse({
  data,
  onReset,
}: {
  data: ResumenSolicitudResponse
  onReset: () => void
}) {
  const handleCopy = () => {
    navigator.clipboard.writeText(buildResumenTxt(data))
    toast.success('Copiado al portapapeles')
  }

  return (
    <motion.div
      key="response-resumen"
      initial={{ opacity: 0, y: 8 }}
      animate={{ opacity: 1, y: 0 }}
      className="flex flex-col gap-4"
    >
      <div className="flex items-center justify-between">
        <h3 className="font-semibold flex items-center gap-2">
          <FileSearch className="h-4 w-4 text-primary" /> Resumen ejecutivo
        </h3>
        <Badge variant="outline" className="text-xs">
          #{data.idSolicitud}
        </Badge>
      </div>

      <p className="text-xs text-muted-foreground">
        Generado: {formatFecha(data.generadoEn)}
      </p>

      <Card className="border-primary/20 bg-primary/5">
        <CardContent className="pt-4">
          <p className="text-sm leading-relaxed whitespace-pre-line">{data.resumen}</p>
        </CardContent>
      </Card>

      <Separator />

      <div className="flex gap-2">
        <Button
          variant="outline"
          size="sm"
          className="flex-1"
          onClick={handleCopy}
        >
          <Copy className="h-4 w-4 mr-1" /> Copiar
        </Button>
        <Button
          variant="outline"
          size="sm"
          className="flex-1"
          onClick={() => downloadTxt(`resumen-${data.idSolicitud}.txt`, buildResumenTxt(data))}
        >
          <Download className="h-4 w-4 mr-1" /> Descargar .txt
        </Button>
      </div>

      <Button variant="ghost" size="sm" onClick={onReset}>
        Nueva consulta
      </Button>
    </motion.div>
  )
}

// ---------------------------------------------------------------------------
// Root export — orchestrates all steps
// ---------------------------------------------------------------------------

export function IaAssistantSheet() {
  const [state, setState] = useState<SheetState>({ step: 'select' })

  const reset = () => setState({ step: 'select' })

  return (
    <div className="flex-1 overflow-y-auto py-4">
      <AnimatePresence mode="wait">
        {state.step === 'select' && (
          <ModeSelector
            key="select"
            onSelect={(mode) => setState({ step: 'form', mode })}
          />
        )}

        {state.step === 'form' && state.mode === 'clasificacion' && (
          <ClasificacionForm
            key="form-clasificacion"
            onBack={reset}
            onSuccess={(data) =>
              setState({ step: 'response', mode: 'clasificacion', data })
            }
          />
        )}

        {state.step === 'form' && state.mode === 'resumen' && (
          <ResumenForm
            key="form-resumen"
            onBack={reset}
            onSuccess={(data) =>
              setState({ step: 'response', mode: 'resumen', data })
            }
          />
        )}

        {state.step === 'response' && state.mode === 'clasificacion' && (
          <ClasificacionResponse
            key="response-clasificacion"
            data={state.data}
            onReset={reset}
          />
        )}

        {state.step === 'response' && state.mode === 'resumen' && (
          <ResumenResponse
            key="response-resumen"
            data={state.data}
            onReset={reset}
          />
        )}
      </AnimatePresence>
    </div>
  )
}
