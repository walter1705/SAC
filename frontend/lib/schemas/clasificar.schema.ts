import { z } from 'zod'

const tiposSolicitud = ['REGISTRO', 'HOMOLOGACION', 'CANCELACION', 'CUPOS', 'CONSULTA'] as const
const prioridades = ['ALTA', 'MEDIA', 'BAJA'] as const

export const clasificarSchema = z.object({
  tipo: z.enum(tiposSolicitud, {
    error: 'Seleccione un tipo de solicitud válido',
  }),
  prioridad: z.enum(prioridades, {
    error: 'Seleccione una prioridad válida',
  }),
  notaClasificacion: z
    .string()
    .min(5, 'Mínimo 5 caracteres')
    .max(500, 'Máximo 500 caracteres'),
})

export type ClasificarFormValues = z.infer<typeof clasificarSchema>
