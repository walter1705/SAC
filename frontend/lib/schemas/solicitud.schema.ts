import { z } from 'zod'

const canalesOrigen = ['CSU', 'EMAIL', 'WEB', 'SAC', 'TELEFONICO', 'PRESENCIAL'] as const

export const crearSolicitudSchema = z.object({
  estudianteNombre: z
    .string()
    .min(2, 'Mínimo 2 caracteres')
    .max(100, 'Máximo 100 caracteres'),
  estudianteCorreo: z
    .string()
    .email('Formato de correo inválido')
    .max(100, 'Máximo 100 caracteres'),
  estudianteTelefono: z
    .string()
    .min(7, 'Mínimo 7 caracteres')
    .max(20, 'Máximo 20 caracteres'),
  estudianteIdentificacion: z
    .string()
    .min(1, 'Este campo es requerido')
    .max(50, 'Máximo 50 caracteres'),
  asunto: z
    .string()
    .min(5, 'Mínimo 5 caracteres')
    .max(200, 'Máximo 200 caracteres'),
  descripcion: z
    .string()
    .min(10, 'Mínimo 10 caracteres')
    .max(2000, 'Máximo 2000 caracteres'),
  canalOrigen: z.enum(canalesOrigen, {
    error: 'Seleccione un canal de origen válido',
  }),
})

export type CrearSolicitudFormValues = z.infer<typeof crearSolicitudSchema>
