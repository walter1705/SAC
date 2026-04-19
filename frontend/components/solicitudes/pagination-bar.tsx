'use client'

import { Button } from '@/components/ui/button'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { ChevronLeft, ChevronRight } from 'lucide-react'

interface PaginationBarProps {
  page: number
  totalPaginas: number
  totalElementos: number
  tamanio: number
  onPageChange: (page: number) => void
  onSizeChange: (size: number) => void
}

export function PaginationBar({
  page,
  totalPaginas,
  totalElementos,
  tamanio,
  onPageChange,
  onSizeChange,
}: PaginationBarProps) {
  const start = page * tamanio + 1
  const end = Math.min((page + 1) * tamanio, totalElementos)

  return (
    <div className="flex items-center justify-between py-4">
      <p className="text-sm text-muted-foreground">
        Mostrando {start}–{end} de {totalElementos}
      </p>

      <div className="flex items-center gap-4">
        <div className="flex items-center gap-2">
          <span className="text-sm text-muted-foreground">Por página:</span>
          <Select
            value={String(tamanio)}
            onValueChange={(v) => onSizeChange(Number(v))}
          >
            <SelectTrigger className="w-[70px] h-8">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="10">10</SelectItem>
              <SelectItem value="20">20</SelectItem>
              <SelectItem value="50">50</SelectItem>
            </SelectContent>
          </Select>
        </div>

        <div className="flex items-center gap-1">
          <Button
            variant="outline"
            size="icon"
            className="h-8 w-8"
            disabled={page === 0}
            onClick={() => onPageChange(page - 1)}
            aria-label="Página anterior"
          >
            <ChevronLeft className="h-4 w-4" />
          </Button>
          <span className="text-sm px-2">
            {page + 1} / {totalPaginas || 1}
          </span>
          <Button
            variant="outline"
            size="icon"
            className="h-8 w-8"
            disabled={page >= totalPaginas - 1}
            onClick={() => onPageChange(page + 1)}
            aria-label="Página siguiente"
          >
            <ChevronRight className="h-4 w-4" />
          </Button>
        </div>
      </div>
    </div>
  )
}
