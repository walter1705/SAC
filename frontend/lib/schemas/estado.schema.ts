import { z } from 'zod'

const estadosSolicitud = ['REGISTRADA', 'CLASIFICADA', 'EN_ATENCION', 'ATENDIDA', 'CERRADA'] as const

export const cambiarEstadoSchema = z.object({
  nuevoEstado: z.enum(estadosSolicitud, {
    error: 'Seleccione un estado válido',
  }),
  nota: z
    .string()
    .max(1000, 'Máximo 1000 caracteres')
    .optional(),
})

export type CambiarEstadoFormValues = z.infer<typeof cambiarEstadoSchema>
