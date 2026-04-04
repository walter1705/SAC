import type { LoginRequest, LoginResponse } from '@/lib/types'

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL

export async function login(body: LoginRequest): Promise<LoginResponse> {
  const response = await fetch(`${API_BASE_URL}/usuarios/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  })

  const data = await response.json()

  if (!response.ok) {
    throw new Error(data.message || 'Error de autenticación')
  }

  return data as LoginResponse
}
