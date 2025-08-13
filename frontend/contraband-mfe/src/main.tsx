import React from 'react'
import { createRoot } from 'react-dom/client'

export default function ContrabandWidget(){
  return <div>Contraband Widget Loaded</div>
}

if (document.getElementById('root')) {
  createRoot(document.getElementById('root')!).render(<ContrabandWidget />)
}