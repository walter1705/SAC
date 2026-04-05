export function formatFecha(iso: string): string {
  const date = new Date(iso)
  return date.toLocaleString('es-CO', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false,
  })
}

export function formatFechaCorta(iso: string): string {
  const date = new Date(iso)
  return date.toLocaleDateString('es-CO', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  })
}

export function formatConfianza(valor: number): string {
  return `${Math.round(valor * 100)}%`
}
