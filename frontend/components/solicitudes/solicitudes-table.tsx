'use client'

import { useRouter, useSearchParams } from 'next/navigation'
import { useCallback, useMemo } from 'react'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import { EstadoBadge } from '@/components/shared/estado-badge'
import { PrioridadBadge } from '@/components/shared/prioridad-badge'
import { TableSkeleton } from '@/components/shared/loading-skeleton'
import { EmptyState } from '@/components/shared/empty-state'
import { SolicitudFilters } from './solicitud-filters'
import { PaginationBar } from './pagination-bar'
import { useSolicitudes } from '@/hooks/use-solicitudes'
import { formatFecha } from '@/lib/utils/formatters'
import { TIPO_LABELS } from '@/lib/utils/constants'
import type { EstadoSolicitud, TipoSolicitud, Prioridad, SolicitudFilterParams } from '@/lib/types'

export function SolicitudesTable() {
  const router = useRouter()
  const searchParams = useSearchParams()

  // Read filters from URL
  const filters: SolicitudFilterParams = useMemo(() => ({
    estado: searchParams.get('estado')
      ? [searchParams.get('estado') as EstadoSolicitud]
      : undefined,
    tipo: searchParams.get('tipo')
      ? [searchParams.get('tipo') as TipoSolicitud]
      : undefined,
    prioridad: searchParams.get('prioridad')
      ? [searchParams.get('prioridad') as Prioridad]
      : undefined,
    page: Number(searchParams.get('page') ?? 0),
    size: Number(searchParams.get('size') ?? 20),
    sort: searchParams.get('sort') ?? 'fechaHoraRegistro,desc',
  }), [searchParams])

  const { data, isLoading } = useSolicitudes(filters)

  // Update URL when filters change
  const updateParams = useCallback(
    (updates: Record<string, string | undefined>) => {
      const params = new URLSearchParams(searchParams.toString())
      Object.entries(updates).forEach(([key, value]) => {
        if (value) {
          params.set(key, value)
        } else {
          params.delete(key)
        }
      })
      router.replace(`/solicitudes?${params.toString()}`)
    },
    [router, searchParams]
  )

  const handleFilterChange = useCallback(
    (newFilters: { estado?: EstadoSolicitud; tipo?: TipoSolicitud; prioridad?: Prioridad }) => {
      updateParams({
        estado: newFilters.estado,
        tipo: newFilters.tipo,
        prioridad: newFilters.prioridad,
        page: '0', // Reset to first page on filter change
      })
    },
    [updateParams]
  )

  if (isLoading) {
    return (
      <div className="space-y-4">
        <SolicitudFilters
          filters={{
            estado: filters.estado?.[0],
            tipo: filters.tipo?.[0],
            prioridad: filters.prioridad?.[0],
          }}
          onChange={handleFilterChange}
        />
        <TableSkeleton />
      </div>
    )
  }

  const solicitudes = data?.content ?? []
  const isEmpty = solicitudes.length === 0

  return (
    <div className="space-y-4">
      <SolicitudFilters
        filters={{
          estado: filters.estado?.[0],
          tipo: filters.tipo?.[0],
          prioridad: filters.prioridad?.[0],
        }}
        onChange={handleFilterChange}
      />

      {isEmpty ? (
        <EmptyState
          title="No se encontraron solicitudes"
          description="Intentá ajustando los filtros de búsqueda."
          action={{
            label: 'Limpiar filtros',
            onClick: () => handleFilterChange({}),
          }}
        />
      ) : (
        <>
          <div className="rounded-md border">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead className="w-[60px]">ID</TableHead>
                  <TableHead>Asunto</TableHead>
                  <TableHead className="hidden md:table-cell">Estudiante</TableHead>
                  <TableHead>Estado</TableHead>
                  <TableHead>Prioridad</TableHead>
                  <TableHead className="hidden lg:table-cell">Tipo</TableHead>
                  <TableHead className="hidden md:table-cell">Fecha</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {solicitudes.map((s) => (
                  <TableRow
                    key={s.id}
                    className="cursor-pointer"
                    onClick={() => router.push(`/solicitudes/${s.id}`)}
                  >
                    <TableCell className="font-mono text-sm">{s.id}</TableCell>
                    <TableCell className="max-w-[200px] truncate font-medium">
                      {s.asunto}
                    </TableCell>
                    <TableCell className="hidden md:table-cell text-muted-foreground">
                      {s.estudianteNombre}
                    </TableCell>
                    <TableCell><EstadoBadge estado={s.estado} /></TableCell>
                    <TableCell><PrioridadBadge prioridad={s.prioridad} /></TableCell>
                    <TableCell className="hidden lg:table-cell text-muted-foreground">
                      {s.tipo ? TIPO_LABELS[s.tipo] : '—'}
                    </TableCell>
                    <TableCell className="hidden md:table-cell text-muted-foreground text-sm">
                      {formatFecha(s.fechaHoraRegistro)}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>

          {data && (
            <PaginationBar
              page={data.pagina}
              totalPaginas={data.totalPaginas}
              totalElementos={data.totalElementos}
              tamanio={data.tamanio}
              onPageChange={(page) => updateParams({ page: String(page) })}
              onSizeChange={(size) => updateParams({ size: String(size), page: '0' })}
            />
          )}
        </>
      )}
    </div>
  )
}
