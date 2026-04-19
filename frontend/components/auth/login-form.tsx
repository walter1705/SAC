'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { GraduationCap, Loader2, AlertCircle } from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { loginSchema, type LoginFormValues } from '@/lib/schemas/login.schema'
import { login as loginApi } from '@/lib/api/auth'
import { useAuth } from '@/lib/auth/context'
import { decodeToken } from '@/lib/auth/token'

interface LoginFormProps {
  expired?: boolean
}

export function LoginForm({ expired }: LoginFormProps) {
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)
  const router = useRouter()
  const { login } = useAuth()

  const form = useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      nombreUsuario: '',
      contrasena: '',
    },
  })

  async function onSubmit(values: LoginFormValues) {
    setError(null)
    setLoading(true)
    try {
      const response = await loginApi(values)
      login(response.token)

      // Role-based redirect
      const payload = decodeToken(response.token)
      if (payload?.rol === 'ADMINISTRADOR') {
        router.push('/usuarios')
      } else {
        router.push('/solicitudes')
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Credenciales incorrectas')
    } finally {
      setLoading(false)
    }
  }

  return (
    <Card className="w-full max-w-sm">
      <CardHeader className="text-center">
        <div className="flex justify-center mb-2">
          <GraduationCap className="h-10 w-10 text-primary" />
        </div>
        <CardTitle className="text-2xl">SAC</CardTitle>
        <CardDescription>Sistema de Atención y Clasificación</CardDescription>
      </CardHeader>
      <CardContent>
        {expired && (
          <Alert variant="destructive" className="mb-4">
            <AlertCircle className="h-4 w-4" />
            <AlertDescription>Sesión expirada. Ingresá nuevamente.</AlertDescription>
          </Alert>
        )}
        {error && (
          <Alert variant="destructive" className="mb-4">
            <AlertCircle className="h-4 w-4" />
            <AlertDescription>{error}</AlertDescription>
          </Alert>
        )}
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="nombreUsuario"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Usuario</FormLabel>
                  <FormControl>
                    <Input placeholder="nombre de usuario" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="contrasena"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Contraseña</FormLabel>
                  <FormControl>
                    <Input type="password" placeholder="••••••••" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <Button type="submit" className="w-full" disabled={loading}>
              {loading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
              Iniciar sesión
            </Button>
          </form>
        </Form>
      </CardContent>
    </Card>
  )
}
