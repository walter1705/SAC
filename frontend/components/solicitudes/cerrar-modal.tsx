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
import { Textarea } from '@/components/ui/textarea'
import { Button } from '@/components/ui/button'
import { cerrarSchema, type CerrarFormValues } from '@/lib/schemas/cerrar.schema'
import { useCerrar } from '@/hooks/use-solicitudes'

interface Props {
  solicitudId: number
}

export function CerrarModal({ solicitudId }: Props) {
  const [open, setOpen] = useState(false)
  const cerrar = useCerrar(solicitudId)

  const form = useForm<CerrarFormValues>({
    resolver: zodResolver(cerrarSchema),
    defaultValues: {
      resolucion: '',
      notasCierre: '',
    },
  })

  function onSubmit(values: CerrarFormValues) {
    cerrar.mutate(values, {
      onSuccess: () => {
        setOpen(false)
        form.reset()
      },
    })
  }

  return (
    <>
      <Button variant="destructive" onClick={() => setOpen(true)}>
        Cerrar solicitud
      </Button>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle>Cerrar solicitud</DialogTitle>
          </DialogHeader>

          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
              <FormField
                control={form.control}
                name="resolucion"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Resolución</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="Descripción de la resolución final (mínimo 10 caracteres)..."
                        className="min-h-[120px]"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="notasCierre"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Notas de cierre (opcional)</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="Observaciones adicionales sobre el cierre..."
                        className="min-h-[80px]"
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
                  disabled={cerrar.isPending}
                >
                  Cancelar
                </Button>
                <Button type="submit" variant="destructive" disabled={cerrar.isPending}>
                  {cerrar.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                  Cerrar solicitud
                </Button>
              </DialogFooter>
            </form>
          </Form>
        </DialogContent>
      </Dialog>
    </>
  )
}
