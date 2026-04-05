'use client'

import { useState } from 'react'
import { motion } from 'framer-motion'
import { Bot } from 'lucide-react'
import { Button } from '@/components/ui/button'
import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet'
import { IaAssistantSheet } from './ia-assistant-sheet'
import { useAuth } from '@/lib/auth/context'

export function IaFloatingButton() {
  const { user } = useAuth()
  const [open, setOpen] = useState(false)

  if (user?.rol !== 'GESTOR') return null

  return (
    <>
      <motion.div
        className="fixed bottom-6 right-6 z-50"
        initial={{ scale: 0, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        transition={{ type: 'spring', stiffness: 260, damping: 20, delay: 0.6 }}
      >
        <Button
          size="icon"
          className="h-14 w-14 rounded-full shadow-lg shadow-primary/30 hover:shadow-primary/50 hover:scale-105 transition-transform"
          onClick={() => setOpen(true)}
          aria-label="Abrir asistente IA"
        >
          <Bot className="h-6 w-6" />
        </Button>
      </motion.div>

      <Sheet open={open} onOpenChange={setOpen}>
        <SheetContent className="w-full sm:max-w-md flex flex-col gap-0">
          <SheetHeader className="border-b pb-4">
            <SheetTitle className="flex items-center gap-2">
              <Bot className="h-5 w-5 text-primary" />
              Asistente IA
            </SheetTitle>
          </SheetHeader>
          <IaAssistantSheet />
        </SheetContent>
      </Sheet>
    </>
  )
}
