import { apiClient } from '@/lib/api/client'
import type { LoginRequest, LoginResponse } from '@/lib/types'

export async function login(body: LoginRequest): Promise<LoginResponse> {
  return apiClient<LoginResponse>('/usuarios/login', {
    method: 'POST',
    body: JSON.stringify(body),
  })
}
