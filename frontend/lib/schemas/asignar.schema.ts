import { z } from 'zod'

export const asignarSchema = z.object({
  responsableId: z
    .number({ error: 'Este campo es requerido' })
    .int('Debe ser un número entero')
    .positive('Debe ser un valor positivo'),
  notaAsignacion: z
    .string()
    .max(500, 'Máximo 500 caracteres')
    .optional(),
})

export type AsignarFormValues = z.infer<typeof asignarSchema>
