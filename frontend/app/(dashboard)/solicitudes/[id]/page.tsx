import { SolicitudDetailCard } from '@/components/solicitudes/solicitud-detail-card'

export default async function SolicitudDetailPage({
  params,
}: {
  params: Promise<{ id: string }>
}) {
  const { id } = await params
  return <SolicitudDetailCard id={Number(id)} />
}
