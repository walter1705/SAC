import Link from 'next/link'
import { Plus } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { SolicitudesTable } from '@/components/solicitudes/solicitudes-table'
import { Suspense } from 'react'
import { TableSkeleton } from '@/components/shared/loading-skeleton'

export default function SolicitudesPage() {
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Solicitudes</h1>
        <Button asChild>
          <Link href="/solicitudes/nueva">
            <Plus className="h-4 w-4 mr-2" />
            Nueva solicitud
          </Link>
        </Button>
      </div>
      <Suspense fallback={<TableSkeleton />}>
        <SolicitudesTable />
      </Suspense>
    </div>
  )
}
