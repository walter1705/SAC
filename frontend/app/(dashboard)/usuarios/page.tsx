import { RoleGate } from '@/lib/auth/guards'
import { UsuariosTable } from '@/components/usuarios/usuarios-table'

export default function UsuariosPage() {
  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Gestión de usuarios</h1>
      <RoleGate allowedRoles={['ADMINISTRADOR']}>
        <UsuariosTable />
      </RoleGate>
    </div>
  )
}
