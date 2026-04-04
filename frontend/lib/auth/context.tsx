'use client'

import { createContext, useContext, useState, useEffect, useCallback } from 'react'
import { useRouter } from 'next/navigation'
import type { RolUsuario } from '@/lib/types'
import { decodeToken, getStoredToken, setStoredToken, removeStoredToken } from './token'

interface AuthUser {
  nombreUsuario: string
  rol: RolUsuario
  userId: number
}

interface AuthContextType {
  user: AuthUser | null
  isAuthenticated: boolean
  login: (token: string) => void
  logout: () => void
}

const AuthContext = createContext<AuthContextType | null>(null)

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(() => {
    // Lazy init: only runs on the client; on the server getStoredToken returns null
    const token = getStoredToken()
    if (!token) return null
    const payload = decodeToken(token)
    if (!payload) return null
    return {
      nombreUsuario: payload.sub,
      rol: payload.rol,
      userId: payload.userId,
    }
  })
  const [isHydrated, setIsHydrated] = useState(false)
  const router = useRouter()

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    setIsHydrated(true)
  }, [])

  const login = useCallback((token: string) => {
    setStoredToken(token)
    const payload = decodeToken(token)
    if (payload) {
      setUser({
        nombreUsuario: payload.sub,
        rol: payload.rol,
        userId: payload.userId,
      })
    }
  }, [])

  const logout = useCallback(() => {
    removeStoredToken()
    setUser(null)
    router.push('/login')
  }, [router])

  // Don't render children until hydrated to avoid flash
  if (!isHydrated) {
    return null
  }

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider')
  }
  return context
}
