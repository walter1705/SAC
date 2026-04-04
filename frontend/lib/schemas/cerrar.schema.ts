import { z } from 'zod'

export const cerrarSchema = z.object({
  resolucion: z
    .string()
    .min(10, 'Mínimo 10 caracteres')
    .max(2000, 'Máximo 2000 caracteres'),
  notasCierre: z
    .string()
    .max(1000, 'Máximo 1000 caracteres')
    .optional(),
})

export type CerrarFormValues = z.infer<typeof cerrarSchema>
