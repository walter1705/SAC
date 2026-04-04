import { LoginForm } from '@/components/auth/login-form'

export default async function LoginPage({
  searchParams,
}: {
  searchParams: Promise<{ expired?: string }>
}) {
  const params = await searchParams
  return <LoginForm expired={params.expired === 'true'} />
}
