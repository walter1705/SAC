import type { TokenPayload } from '@/lib/types'

const TOKEN_KEY = 'sac_token'
const AUTH_COOKIE = 'sac_auth'

export function decodeToken(token: string): TokenPayload | null {
  try {
    const payload = token.split('.')[1]
    return JSON.parse(atob(payload))
  } catch {
    return null
  }
}

export function isTokenExpired(token: string): boolean {
  const payload = decodeToken(token)
  if (!payload) return true
  return payload.exp * 1000 < Date.now()
}

export function getStoredToken(): string | null {
  if (typeof window === 'undefined') return null
  const token = localStorage.getItem(TOKEN_KEY)
  if (!token) return null
  if (isTokenExpired(token)) {
    removeStoredToken()
    return null
  }
  return token
}

export function setStoredToken(token: string): void {
  localStorage.setItem(TOKEN_KEY, token)
  // Set cookie mirror for middleware
  document.cookie = `${AUTH_COOKIE}=1; path=/; max-age=${60 * 60}; SameSite=Lax`
}

export function removeStoredToken(): void {
  localStorage.removeItem(TOKEN_KEY)
  document.cookie = `${AUTH_COOKIE}=; path=/; max-age=0`
}
