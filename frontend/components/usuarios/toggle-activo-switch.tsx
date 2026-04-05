'use client'

import { useState } from 'react'
import { Switch } from '@/components/ui/switch'
import { ConfirmDialog } from '@/components/shared/confirm-dialog'
import { useCambiarEstadoUsuario } from '@/hooks/use-usuarios'

interface Props {
  userId: number
  nombreCompleto: string
  activo: boolean
}

export function ToggleActivoSwitch({ userId, nombreCompleto, activo }: Props) {
  const [showConfirm, setShowConfirm] = useState(false)
  const mutation = useCambiarEstadoUsuario()

  const handleToggle = (checked: boolean) => {
    if (!checked) {
      setShowConfirm(true)
    } else {
      mutation.mutate({ id: userId, body: { activo: true } })
    }
  }

  const handleConfirm = () => {
    mutation.mutate(
      { id: userId, body: { activo: false } },
      { onSuccess: () => setShowConfirm(false) }
    )
  }

  return (
    <>
      <Switch
        checked={activo}
        onCheckedChange={handleToggle}
        disabled={mutation.isPending}
        aria-label={`${activo ? 'Desactivar' : 'Activar'} a ${nombreCompleto}`}
      />
      <ConfirmDialog
        open={showConfirm}
        onOpenChange={setShowConfirm}
        title={`¿Desactivar a ${nombreCompleto}?`}
        description="Esta acción no puede deshacerse desde esta interfaz. Una vez desactivado, el usuario no aparecerá en la lista."
        confirmLabel="Desactivar"
        variant="destructive"
        onConfirm={handleConfirm}
        loading={mutation.isPending}
      />
    </>
  )
}
