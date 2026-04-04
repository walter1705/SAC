import Link from 'next/link'
import { Button } from '@/components/ui/button'
import { FileQuestion } from 'lucide-react'

export default function NotFound() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center text-center p-4">
      <FileQuestion className="h-16 w-16 text-muted-foreground mb-4" />
      <h1 className="text-2xl font-bold">Página no encontrada</h1>
      <p className="text-muted-foreground mt-2">
        La página que buscás no existe o fue movida.
      </p>
      <Button asChild className="mt-6">
        <Link href="/solicitudes">Ir a solicitudes</Link>
      </Button>
    </div>
  )
}
