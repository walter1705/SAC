'use client'

import { Button } from '@/components/ui/button'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { X } from 'lucide-react'
import { ESTADO_LABELS, TIPO_LABELS, PRIORIDAD_LABELS } from '@/lib/utils/constants'
import type { EstadoSolicitud, TipoSolicitud, Prioridad } from '@/lib/types'

interface FiltersState {
  estado?: EstadoSolicitud
  tipo?: TipoSolicitud
  prioridad?: Prioridad
}

interface SolicitudFiltersProps {
  filters: FiltersState
  onChange: (filters: FiltersState) => void
}

export function SolicitudFilters({ filters, onChange }: SolicitudFiltersProps) {
  const hasFilters = filters.estado || filters.tipo || filters.prioridad

  return (
    <div className="flex flex-wrap items-center gap-3">
      <Select
        value={filters.estado ?? ''}
        onValueChange={(value) =>
          onChange({ ...filters, estado: value ? (value as EstadoSolicitud) : undefined })
        }
      >
        <SelectTrigger className="w-[180px]">
          <SelectValue placeholder="Estado" />
        </SelectTrigger>
        <SelectContent>
          {Object.entries(ESTADO_LABELS).map(([key, label]) => (
            <SelectItem key={key} value={key}>{label}</SelectItem>
          ))}
        </SelectContent>
      </Select>

      <Select
        value={filters.tipo ?? ''}
        onValueChange={(value) =>
          onChange({ ...filters, tipo: value ? (value as TipoSolicitud) : undefined })
        }
      >
        <SelectTrigger className="w-[180px]">
          <SelectValue placeholder="Tipo" />
        </SelectTrigger>
        <SelectContent>
          {Object.entries(TIPO_LABELS).map(([key, label]) => (
            <SelectItem key={key} value={key}>{label}</SelectItem>
          ))}
        </SelectContent>
      </Select>

      <Select
        value={filters.prioridad ?? ''}
        onValueChange={(value) =>
          onChange({ ...filters, prioridad: value ? (value as Prioridad) : undefined })
        }
      >
        <SelectTrigger className="w-[180px]">
          <SelectValue placeholder="Prioridad" />
        </SelectTrigger>
        <SelectContent>
          {Object.entries(PRIORIDAD_LABELS).map(([key, label]) => (
            <SelectItem key={key} value={key}>{label}</SelectItem>
          ))}
        </SelectContent>
      </Select>

      {hasFilters && (
        <Button
          variant="ghost"
          size="sm"
          onClick={() => onChange({})}
        >
          <X className="h-4 w-4 mr-1" />
          Limpiar filtros
        </Button>
      )}
    </div>
  )
}
