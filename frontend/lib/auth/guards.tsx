'use client'

import { useAuth } from './context'
import type { RolUsuario } from '@/lib/types'
import { Button } from '@/components/ui/button'
import { ShieldAlert } from 'lucide-react'
import Link from 'next/link'

interface RoleGateProps {
  allowedRoles: RolUsuario[]
  children: React.ReactNode
  fallback?: React.ReactNode
}

export function RoleGate({ allowedRoles, children, fallback }: RoleGateProps) {
  const { user } = useAuth()

  if (!user || !allowedRoles.includes(user.rol)) {
    return fallback ?? <AccessDenied />
  }

  return <>{children}</>
}

function AccessDenied() {
  return (
    <div className="flex flex-col items-center justify-center py-12 text-center">
      <ShieldAlert className="h-12 w-12 text-muted-foreground mb-4" />
      <h3 className="text-lg font-semibold">Acceso denegado</h3>
      <p className="text-sm text-muted-foreground mt-1">
        No tenés permisos para acceder a esta sección.
      </p>
      <Button asChild variant="outline" className="mt-4">
        <Link href="/solicitudes">Volver a solicitudes</Link>
      </Button>
    </div>
  )
}
