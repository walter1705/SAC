import { z } from 'zod'

export const loginSchema = z.object({
  nombreUsuario: z
    .string()
    .min(3, 'Mínimo 3 caracteres')
    .max(50, 'Máximo 50 caracteres'),
  contrasena: z
    .string()
    .min(8, 'Mínimo 8 caracteres')
    .max(100, 'Máximo 100 caracteres'),
})

export type LoginFormValues = z.infer<typeof loginSchema>
