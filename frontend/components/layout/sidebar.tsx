'use client'

import { GraduationCap } from 'lucide-react'
import { NavLinks } from './nav-links'
import type { RolUsuario } from '@/lib/types'

interface SidebarProps {
  rol: RolUsuario | null
}

export function Sidebar({ rol }: SidebarProps) {
  return (
    <aside className="hidden md:flex w-64 flex-col border-r bg-card">
      <div className="flex h-14 items-center gap-2 border-b px-4">
        <GraduationCap className="h-6 w-6 text-primary" />
        <span className="font-semibold text-lg">SAC</span>
      </div>
      <div className="flex-1 p-4">
        <NavLinks rol={rol} />
      </div>
    </aside>
  )
}
