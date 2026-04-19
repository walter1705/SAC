'use client'

import Link from 'next/link'
import { usePathname } from 'next/navigation'
import { FileText, Users } from 'lucide-react'
import { cn } from '@/lib/utils'
import { NAV_LINKS } from '@/lib/utils/constants'
import type { RolUsuario } from '@/lib/types'

const iconMap: Record<string, React.ElementType> = {
  FileText,
  Users,
}

interface NavLinksProps {
  rol: RolUsuario | null
  onNavigate?: () => void
}

export function NavLinks({ rol, onNavigate }: NavLinksProps) {
  const pathname = usePathname()

  const filteredLinks = NAV_LINKS.filter(
    (link) => rol && link.roles.includes(rol)
  )

  return (
    <nav className="flex flex-col gap-1">
      {filteredLinks.map((link) => {
        const Icon = iconMap[link.icon]
        const isActive = pathname.startsWith(link.href)
        return (
          <Link
            key={link.href}
            href={link.href}
            onClick={onNavigate}
            aria-current={isActive ? 'page' : undefined}
            className={cn(
              'flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors',
              isActive
                ? 'bg-primary/10 text-primary'
                : 'text-muted-foreground hover:bg-muted hover:text-foreground'
            )}
          >
            {Icon && <Icon className="h-4 w-4" />}
            {link.label}
          </Link>
        )
      })}
    </nav>
  )
}
