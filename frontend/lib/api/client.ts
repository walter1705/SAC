const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL

if (!API_BASE_URL) {
  throw new Error('NEXT_PUBLIC_API_URL no está configurada')
}

export class ApiError extends Error {
  constructor(
    public status: number,
    message: string,
    public errors?: Record<string, string>
  ) {
    super(message)
    this.name = 'ApiError'
  }
}

export async function apiClient<T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> {
  const headers: HeadersInit = {
    'Content-Type': 'application/json',
    ...options.headers,
  }

  // Inject JWT if available (client-side only)
  if (typeof window !== 'undefined') {
    const token = localStorage.getItem('sac_token')
    if (token) {
      (headers as Record<string, string>)['Authorization'] = `Bearer ${token}`
    }
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  })

  // Handle 401 — clear token, redirect
  if (response.status === 401) {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('sac_token')
      document.cookie = 'sac_auth=; path=/; max-age=0'
      window.location.href = '/login?expired=true'
    }
    throw new ApiError(401, 'Sesión expirada')
  }

  // Handle 204 No Content
  if (response.status === 204) {
    return undefined as T
  }

  const data = await response.json()

  // Handle error responses
  if (!response.ok) {
    throw new ApiError(
      response.status,
      data.message || 'Ocurrió un error inesperado',
      data.errors
    )
  }

  return data as T
}
