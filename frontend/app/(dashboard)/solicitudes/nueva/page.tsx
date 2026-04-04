import { SolicitudForm } from '@/components/solicitudes/solicitud-form'

export default function NuevaSolicitudPage() {
  return (
    <div className="max-w-3xl mx-auto space-y-6">
      <h1 className="text-2xl font-bold">Nueva solicitud</h1>
      <SolicitudForm />
    </div>
  )
}
