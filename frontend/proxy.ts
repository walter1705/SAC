import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

export function proxy(request: NextRequest) {
  const { pathname } = request.nextUrl
  const hasAuth = request.cookies.get('sac_auth')?.value === '1'

  // Unauthenticated trying to access dashboard → login
  if (!hasAuth && !pathname.startsWith('/login')) {
    return NextResponse.redirect(new URL('/login', request.url))
  }

  // Authenticated trying to access login → dashboard
  if (hasAuth && pathname.startsWith('/login')) {
    return NextResponse.redirect(new URL('/solicitudes', request.url))
  }

  return NextResponse.next()
}

export const config = {
  matcher: [
    /*
     * Match all paths except:
     * - api routes
     * - _next static files
     * - _next image optimization
     * - favicon.ico
     * - public files
     */
    '/((?!api|_next/static|_next/image|favicon.ico|.*\\.(?:svg|png|jpg|jpeg|gif|webp)$).*)',
  ],
}
