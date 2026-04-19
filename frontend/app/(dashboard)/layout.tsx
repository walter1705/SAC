'use client'

import { Sidebar } from '@/components/layout/sidebar'
import { Header } from '@/components/layout/header'
import { useAuth } from '@/lib/auth/context'
import { PageTransition } from '@/components/shared/page-transition'
import { IaFloatingButton } from '@/components/ia/ia-floating-button'

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode
}) {
  const { user, logout } = useAuth()

  return (
    <div className="flex h-screen">
      <Sidebar rol={user?.rol ?? null} />
      <div className="flex flex-1 flex-col overflow-hidden">
        <Header
          nombreUsuario={user?.nombreUsuario ?? null}
          rol={user?.rol ?? null}
          onLogout={logout}
        />
        <main className="flex-1 overflow-y-auto p-6">
          <PageTransition>{children}</PageTransition>
        </main>
      </div>
      <IaFloatingButton />
    </div>
  )
}
