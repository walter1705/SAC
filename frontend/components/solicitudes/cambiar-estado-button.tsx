'use client'

import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { Loader2 } from 'lucide-react'
import { Button } from '@/components/ui/button'
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
import { Textarea } from '@/components/ui/textarea'
import { cambiarEstadoSchema, type CambiarEstadoFormValues } from '@/lib/schemas/estado.schema'
import { useCambiarEstado } from '@/hooks/use-solicitudes'
import type { EstadoSolicitud } from '@/lib/types'

interface Props {
  solicitudId: number
  nuevoEstado: EstadoSolicitud
  label: string
}

export function CambiarEstadoButton({ solicitudId, nuevoEstado, label }: Props) {
  const [open, setOpen] = useState(false)
  const cambiarEstado = useCambiarEstado(solicitudId)

  const form = useForm<CambiarEstadoFormValues>({
    resolver: zodResolver(cambiarEstadoSchema),
    defaultValues: {
      nuevoEstado,
      nota: '',
    },
  })

  function onSubmit(values: CambiarEstadoFormValues) {
    cambiarEstado.mutate(values, {
      onSuccess: () => {
        setOpen(false)
        form.reset({ nuevoEstado, nota: '' })
      },
    })
  }

  return (
    <>
      <Button variant="outline" onClick={() => setOpen(true)}>
        {label}
      </Button>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle>{label}</DialogTitle>
          </DialogHeader>

          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
              <FormField
                control={form.control}
                name="nota"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Nota (opcional)</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="Observaciones sobre este cambio de estado..."
                        className="min-h-[100px]"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <DialogFooter>
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => setOpen(false)}
                  disabled={cambiarEstado.isPending}
                >
                  Cancelar
                </Button>
                <Button type="submit" disabled={cambiarEstado.isPending}>
                  {cambiarEstado.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                  Confirmar
                </Button>
              </DialogFooter>
            </form>
          </Form>
        </DialogContent>
      </Dialog>
    </>
  )
}
