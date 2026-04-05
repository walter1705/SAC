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
import { asignarSchema, type AsignarFormValues } from '@/lib/schemas/asignar.schema'
import { useAsignar } from '@/hooks/use-solicitudes'
import { useUsuarios } from '@/hooks/use-usuarios'

interface Props {
  solicitudId: number
}

export function AsignarModal({ solicitudId }: Props) {
  const [open, setOpen] = useState(false)
  const asignar = useAsignar(solicitudId)
  const { data: usuarios, isLoading: loadingUsuarios } = useUsuarios()

  const form = useForm<AsignarFormValues>({
    resolver: zodResolver(asignarSchema),
    defaultValues: {
      responsableId: undefined,
      notaAsignacion: '',
    },
  })

  function onSubmit(values: AsignarFormValues) {
    asignar.mutate(values, {
      onSuccess: () => {
        setOpen(false)
        form.reset()
      },
    })
  }

  // Filter only active GESTOR users as candidates for assignment
  const candidatos = usuarios?.filter((u) => u.activo && u.rol === 'GESTOR') ?? []

  return (
    <>
      <Button variant="outline" onClick={() => setOpen(true)}>
        Asignar responsable
      </Button>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle>Asignar responsable</DialogTitle>
          </DialogHeader>

          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
              <FormField
                control={form.control}
                name="responsableId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Responsable</FormLabel>
                    <Select
                      onValueChange={(val) => field.onChange(Number(val))}
                      value={field.value ? String(field.value) : undefined}
                      disabled={loadingUsuarios}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue
                            placeholder={
                              loadingUsuarios ? 'Cargando usuarios...' : 'Seleccionar responsable'
                            }
                          />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {candidatos.map((usuario) => (
                          <SelectItem key={usuario.id} value={String(usuario.id)}>
                            {usuario.nombreCompleto}
                          </SelectItem>
                        ))}
                        {!loadingUsuarios && candidatos.length === 0 && (
                          <SelectItem value="__none__" disabled>
                            No hay gestores disponibles
                          </SelectItem>
                        )}
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="notaAsignacion"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Nota de asignación (opcional)</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="Observaciones sobre la asignación..."
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
                  disabled={asignar.isPending}
                >
                  Cancelar
                </Button>
                <Button type="submit" disabled={asignar.isPending || loadingUsuarios}>
                  {asignar.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                  Asignar
                </Button>
              </DialogFooter>
            </form>
          </Form>
        </DialogContent>
      </Dialog>
    </>
  )
}
