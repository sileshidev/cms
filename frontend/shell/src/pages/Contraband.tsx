import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { useState } from 'react'

const API = (path: string) => `http://localhost:8082${path}`

export default function Contraband(){
  const qc = useQueryClient()
  const { data } = useQuery({ queryKey: ['contraband'], queryFn: async () => {
    const res = await fetch(API('/api/contraband'))
    return res.json()
  }})
  const [form, setForm] = useState({ type:'', category:'', quantity:1, unit:'pcs', serialNumber:'' })
  const create = useMutation({ mutationFn: async () => {
    const res = await fetch(API('/api/contraband'), { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify(form) })
    return res.json()
  }, onSuccess:() => qc.invalidateQueries({ queryKey:['contraband'] })})

  return <div style={{padding:16}}>
    <h2>Contraband</h2>
    <div>
      <input placeholder='type' value={form.type} onChange={e=>setForm({...form,type:e.target.value})}/>
      <input placeholder='category' value={form.category} onChange={e=>setForm({...form,category:e.target.value})}/>
      <input type='number' placeholder='quantity' value={form.quantity} onChange={e=>setForm({...form,quantity:Number(e.target.value)})}/>
      <input placeholder='unit' value={form.unit} onChange={e=>setForm({...form,unit:e.target.value})}/>
      <input placeholder='serialNumber' value={form.serialNumber} onChange={e=>setForm({...form,serialNumber:e.target.value})}/>
      <button onClick={()=>create.mutate()}>Create</button>
    </div>
    <pre>{JSON.stringify(data, null, 2)}</pre>
  </div>
}