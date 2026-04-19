'use client'

import { useAuth } from '@/lib/auth/context'
import { ACCIONES_POR_ESTADO } from '@/lib/utils/constants'
import type { SolicitudResponse } from '@/lib/types'
import { ClasificarModal } from './clasificar-modal'
import { AsignarModal } from './asignar-modal'
import { CerrarModal } from './cerrar-modal'
import { CambiarEstadoButton } from './cambiar-estado-button'

interface Props {
  solicitud: SolicitudResponse
}

export function AccionesPanel({ solicitud }: Props) {
  const { user } = useAuth()

  // Only GESTOR can perform actions
  if (!user || user.rol !== 'GESTOR') {
    return null
  }

  const acciones = ACCIONES_POR_ESTADO[solicitud.estado]

  if (acciones.length === 0) {
    return null
  }

  return (
    <div className="flex flex-wrap gap-2">
      {acciones.map((accion) => {
        switch (accion.key) {
          case 'clasificar':
            return <ClasificarModal key={accion.key} solicitud={solicitud} />
          case 'asignar':
            return <AsignarModal key={accion.key} solicitudId={solicitud.id} />
          case 'iniciar':
            return (
              <CambiarEstadoButton
                key={accion.key}
                solicitudId={solicitud.id}
                nuevoEstado="EN_ATENCION"
                label="Iniciar atención"
              />
            )
          case 'atender':
            return (
              <CambiarEstadoButton
                key={accion.key}
                solicitudId={solicitud.id}
                nuevoEstado="ATENDIDA"
                label="Marcar como atendida"
              />
            )
          case 'cerrar':
            return <CerrarModal key={accion.key} solicitudId={solicitud.id} />
          default:
            return null
        }
      })}
    </div>
  )
}
