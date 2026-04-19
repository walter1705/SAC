'use client'

import { useState } from 'react'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { UserPlus } from 'lucide-react'
import { TableSkeleton } from '@/components/shared/loading-skeleton'
import { EmptyState } from '@/components/shared/empty-state'
import { ToggleActivoSwitch } from './toggle-activo-switch'
import { CrearUsuarioModal } from './crear-usuario-modal'
import { useUsuarios } from '@/hooks/use-usuarios'
import { ROL_LABELS } from '@/lib/utils/constants'
import type { RolUsuario } from '@/lib/types'

const ROL_BADGE_VARIANT: Record<RolUsuario, 'destructive' | 'default' | 'secondary'> = {
  ADMINISTRADOR: 'destructive',
  GESTOR: 'default',
  SOLICITANTE: 'secondary',
}

export function UsuariosTable() {
  const [modalOpen, setModalOpen] = useState(false)
  const { data: usuarios, isLoading, isError } = useUsuarios()

  return (
    <div className="space-y-4">
      <div className="flex justify-end">
        <Button onClick={() => setModalOpen(true)}>
          <UserPlus className="mr-2 h-4 w-4" />
          Crear usuario
        </Button>
      </div>

      {isLoading && <TableSkeleton rows={6} cols={5} />}

      {isError && (
        <EmptyState
          title="Error al cargar usuarios"
          description="No se pudo obtener la lista de usuarios. Intentá de nuevo."
        />
      )}

      {!isLoading && !isError && usuarios && usuarios.length === 0 && (
        <EmptyState
          title="No hay usuarios registrados"
          description="Creá el primer usuario con el botón de arriba."
        />
      )}

      {!isLoading && !isError && usuarios && usuarios.length > 0 && (
        <div className="rounded-md border">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Nombre completo</TableHead>
                <TableHead>Usuario</TableHead>
                <TableHead>Correo electrónico</TableHead>
                <TableHead>Rol</TableHead>
                <TableHead className="text-center">Activo</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {usuarios.map((usuario) => (
                <TableRow key={usuario.id}>
                  <TableCell className="font-medium">{usuario.nombreCompleto}</TableCell>
                  <TableCell className="text-muted-foreground">{usuario.nombreUsuario}</TableCell>
                  <TableCell>{usuario.email}</TableCell>
                  <TableCell>
                    <Badge variant={ROL_BADGE_VARIANT[usuario.rol]}>
                      {ROL_LABELS[usuario.rol]}
                    </Badge>
                  </TableCell>
                  <TableCell className="text-center">
                    <ToggleActivoSwitch
                      userId={usuario.id}
                      nombreCompleto={usuario.nombreCompleto}
                      activo={usuario.activo}
                    />
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      )}

      <CrearUsuarioModal open={modalOpen} onOpenChange={setModalOpen} />
    </div>
  )
}
