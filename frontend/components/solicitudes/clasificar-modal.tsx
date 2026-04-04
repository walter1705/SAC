'use client'

import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { Loader2 } from 'lucide-react'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { Textarea } from '@/components/ui/textarea'
import { Button } from '@/components/ui/button'
import { clasificarSchema, type ClasificarFormValues } from '@/lib/schemas/clasificar.schema'
import { useClasificar } from '@/hooks/use-solicitudes'
import { TIPO_LABELS, PRIORIDAD_LABELS } from '@/lib/utils/constants'
import type { SolicitudResponse, TipoSolicitud, Prioridad } from '@/lib/types'
import { IaSuggestionPanel } from './ia-suggestion-panel'

interface Props {
  solicitud: SolicitudResponse
}

export function ClasificarModal({ solicitud }: Props) {
  const [open, setOpen] = useState(false)
  const clasificar = useClasificar(solicitud.id)

  const form = useForm<ClasificarFormValues>({
    resolver: zodResolver(clasificarSchema),
    defaultValues: {
      tipo: solicitud.tipo ?? undefined,
      prioridad: solicitud.prioridad ?? undefined,
      notaClasificacion: solicitud.notaClasificacion ?? '',
    },
  })

  function onSubmit(values: ClasificarFormValues) {
    clasificar.mutate(values, {
      onSuccess: () => {
        setOpen(false)
        form.reset()
      },
    })
  }

  return (
    <>
      <Button onClick={() => setOpen(true)}>Clasificar</Button>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle>Clasificar solicitud</DialogTitle>
          </DialogHeader>

          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
              <FormField
                control={form.control}
                name="tipo"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Tipo de solicitud</FormLabel>
                    <Select onValueChange={field.onChange} value={field.value}>
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Seleccionar tipo" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {(Object.entries(TIPO_LABELS) as [TipoSolicitud, string][]).map(
                          ([value, label]) => (
                            <SelectItem key={value} value={value}>
                              {label}
                            </SelectItem>
                          )
                        )}
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="prioridad"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Prioridad</FormLabel>
                    <Select onValueChange={field.onChange} value={field.value}>
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Seleccionar prioridad" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {(Object.entries(PRIORIDAD_LABELS) as [Prioridad, string][]).map(
                          ([value, label]) => (
                            <SelectItem key={value} value={value}>
                              {label}
                            </SelectItem>
                          )
                        )}
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="notaClasificacion"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Nota de clasificación</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="Justificación de la clasificación..."
                        className="min-h-[100px]"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <div className="pt-1">
                <IaSuggestionPanel
                  descripcion={solicitud.descripcion}
                  onApply={(tipo, prioridad) => {
                    form.setValue('tipo', tipo)
                    form.setValue('prioridad', prioridad)
                  }}
                />
              </div>

              <DialogFooter>
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => setOpen(false)}
                  disabled={clasificar.isPending}
                >
                  Cancelar
                </Button>
                <Button type="submit" disabled={clasificar.isPending}>
                  {clasificar.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                  Clasificar
                </Button>
              </DialogFooter>
            </form>
          </Form>
        </DialogContent>
      </Dialog>
    </>
  )
}
