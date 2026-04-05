'use client'

import { Menu, LogOut } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { ThemeToggle } from './theme-toggle'
import { Sheet, SheetContent, SheetTrigger, SheetTitle } from '@/components/ui/sheet'
import { NavLinks } from './nav-links'
import { GraduationCap } from 'lucide-react'
import type { RolUsuario } from '@/lib/types'
import { useState } from 'react'

interface HeaderProps {
  nombreUsuario: string | null
  rol: RolUsuario | null
  onLogout: () => void
}

export function Header({ nombreUsuario, rol, onLogout }: HeaderProps) {
  const [sheetOpen, setSheetOpen] = useState(false)

  return (
    <header className="flex h-14 items-center gap-4 border-b bg-card px-4">
      {/* Mobile hamburger */}
      <Sheet open={sheetOpen} onOpenChange={setSheetOpen}>
        <SheetTrigger asChild>
          <Button variant="ghost" size="icon" className="md:hidden" aria-label="Abrir menú">
            <Menu className="h-5 w-5" />
          </Button>
        </SheetTrigger>
        <SheetContent side="left" className="w-64 p-0">
          <SheetTitle className="sr-only">Menú de navegación</SheetTitle>
          <div className="flex h-14 items-center gap-2 border-b px-4">
            <GraduationCap className="h-6 w-6 text-primary" />
            <span className="font-semibold text-lg">SAC</span>
          </div>
          <div className="p-4">
            <NavLinks rol={rol} onNavigate={() => setSheetOpen(false)} />
          </div>
        </SheetContent>
      </Sheet>

      <div className="flex-1" />

      {/* User info + actions */}
      <span className="text-sm text-muted-foreground hidden sm:inline">
        {nombreUsuario}
      </span>
      <ThemeToggle />
      <Button variant="ghost" size="icon" aria-label="Cerrar sesión" onClick={onLogout}>
        <LogOut className="h-4 w-4" />
      </Button>
    </header>
  )
}
