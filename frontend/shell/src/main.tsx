import React from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'

const queryClient = new QueryClient()

const Dashboard = React.lazy(() => import('./pages/Dashboard'))
const Contraband = React.lazy(() => import('./pages/Contraband'))

const router = createBrowserRouter([
  { path: '/', element: <React.Suspense fallback={<>...</>}><Dashboard/></React.Suspense> },
  { path: '/contraband', element: <React.Suspense fallback={<>...</>}><Contraband/></React.Suspense> },
])

createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
  </React.StrictMode>
)