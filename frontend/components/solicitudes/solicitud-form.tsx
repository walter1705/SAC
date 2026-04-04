'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { Loader2, AlertCircle } from 'lucide-react'
import { Card, CardContent } from '@/components/ui/card'
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
import { Button } from '@/components/ui/button'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { crearSolicitudSchema, type CrearSolicitudFormValues } from '@/lib/schemas/solicitud.schema'
import { useCrearSolicitud } from '@/hooks/use-solicitudes'
import { CANAL_LABELS } from '@/lib/utils/constants'
import { ApiError } from '@/lib/api/client'
import type { CanalOrigen } from '@/lib/types'

export function SolicitudForm() {
  const [serverError, setServerError] = useState<string | null>(null)
  const router = useRouter()
  const crearMutation = useCrearSolicitud()

  const form = useForm<CrearSolicitudFormValues>({
    resolver: zodResolver(crearSolicitudSchema),
    defaultValues: {
      estudianteNombre: '',
      estudianteCorreo: '',
      estudianteTelefono: '',
      estudianteIdentificacion: '',
      asunto: '',
      descripcion: '',
      canalOrigen: undefined,
    },
  })

  async function onSubmit(values: CrearSolicitudFormValues) {
    setServerError(null)
    try {
      await crearMutation.mutateAsync(values)
      // redirect handled by mutation onSuccess
    } catch (error) {
      if (error instanceof ApiError && error.status === 400) {
        setServerError(error.message)
      }
      // Other errors handled by mutation onError toast
    }
  }

  return (
    <Card>
      <CardContent className="pt-6">
        {serverError && (
          <Alert variant="destructive" className="mb-6">
            <AlertCircle className="h-4 w-4" />
            <AlertDescription>{serverError}</AlertDescription>
          </Alert>
        )}

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
            {/* Student Info Section */}
            <div>
              <h3 className="text-lg font-semibold mb-4">Datos del estudiante</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <FormField
                  control={form.control}
                  name="estudianteNombre"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Nombre completo</FormLabel>
                      <FormControl>
                        <Input placeholder="María González" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="estudianteCorreo"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Correo electrónico</FormLabel>
                      <FormControl>
                        <Input type="email" placeholder="correo@universidad.edu.co" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="estudianteTelefono"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Teléfono</FormLabel>
                      <FormControl>
                        <Input placeholder="3001234567" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="estudianteIdentificacion"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Identificación</FormLabel>
                      <FormControl>
                        <Input placeholder="1094567890" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>
            </div>

            {/* Request Info Section */}
            <div>
              <h3 className="text-lg font-semibold mb-4">Datos de la solicitud</h3>
              <div className="space-y-4">
                <FormField
                  control={form.control}
                  name="asunto"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Asunto</FormLabel>
                      <FormControl>
                        <Input placeholder="Breve descripción del asunto" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="descripcion"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Descripción</FormLabel>
                      <FormControl>
                        <Textarea
                          placeholder="Descripción detallada de la solicitud..."
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
                  name="canalOrigen"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Canal de origen</FormLabel>
                      <Select onValueChange={field.onChange} value={field.value}>
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="Seleccionar canal" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          {(Object.entries(CANAL_LABELS) as [CanalOrigen, string][]).map(
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
              </div>
            </div>

            {/* Actions */}
            <div className="flex gap-3 justify-end">
              <Button
                type="button"
                variant="outline"
                onClick={() => router.push('/solicitudes')}
              >
                Cancelar
              </Button>
              <Button type="submit" disabled={crearMutation.isPending}>
                {crearMutation.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                Crear solicitud
              </Button>
            </div>
          </form>
        </Form>
      </CardContent>
    </Card>
  )
}
