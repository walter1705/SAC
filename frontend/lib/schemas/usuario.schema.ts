import { z } from 'zod'

const rolesUsuario = ['ADMINISTRADOR', 'GESTOR', 'SOLICITANTE'] as const

export const crearUsuarioSchema = z.object({
  nombreCompleto: z
    .string()
    .min(2, 'Mínimo 2 caracteres')
    .max(100, 'Máximo 100 caracteres'),
  nombreUsuario: z
    .string()
    .min(3, 'Mínimo 3 caracteres')
    .max(50, 'Máximo 50 caracteres'),
  contrasena: z
    .string()
    .min(8, 'Mínimo 8 caracteres')
    .max(100, 'Máximo 100 caracteres'),
  email: z
    .string()
    .email('Formato de correo inválido')
    .max(100, 'Máximo 100 caracteres'),
  rol: z.enum(rolesUsuario, {
    error: 'Seleccione un rol válido',
  }),
})

export type CrearUsuarioFormValues = z.infer<typeof crearUsuarioSchema>
