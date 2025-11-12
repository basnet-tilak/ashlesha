import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useState } from 'react'
import axios from 'axios'
import { loginSchema } from '../lib/validation'

const initialState = {
  status: 'idle',
  message: null
}

export default function LoginForm() {
  const [state, setState] = useState(initialState)
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting }
  } = useForm({ resolver: zodResolver(loginSchema) })

  const onSubmit = async (data) => {
    setState({ status: 'loading', message: null })
    try {
      const response = await axios.post('/api/auth/login', data)
      const token = response.data.token
      localStorage.setItem('kuberwallet_token', token)
      setState({ status: 'success', message: 'Authenticated successfully. Redirecting you to your vault...' })
    } catch (error) {
      const message = error.response?.data?.message ?? 'Unable to authenticate. Please check your credentials.'
      setState({ status: 'error', message })
    }
  }

  return (
    <form className="space-y-6" onSubmit={handleSubmit(onSubmit)}>
      <div>
        <label htmlFor="username" className="block text-sm font-medium text-slate-200">
          Email or username
        </label>
        <input
          id="username"
          type="text"
          autoComplete="username"
          className="mt-2 w-full rounded-xl border border-white/10 bg-slate-950/80 px-4 py-3 text-white focus:border-teal-400 focus:outline-none"
          {...register('username')}
        />
        {errors.username && <p className="mt-1 text-sm text-rose-400">{errors.username.message}</p>}
      </div>

      <div>
        <label htmlFor="password" className="block text-sm font-medium text-slate-200">
          Password
        </label>
        <input
          id="password"
          type="password"
          autoComplete="current-password"
          className="mt-2 w-full rounded-xl border border-white/10 bg-slate-950/80 px-4 py-3 text-white focus:border-teal-400 focus:outline-none"
          {...register('password')}
        />
        {errors.password && <p className="mt-1 text-sm text-rose-400">{errors.password.message}</p>}
      </div>

      <button
        type="submit"
        className="w-full rounded-xl bg-primary px-4 py-3 text-sm font-semibold text-white shadow-lg shadow-primary/30 transition hover:bg-primary/90"
        disabled={isSubmitting}
      >
        {isSubmitting ? 'Signing you in…' : 'Access my account'}
      </button>

      {state.message && (
        <div
          className={`rounded-xl px-4 py-3 text-sm ${
            state.status === 'success'
              ? 'bg-emerald-500/10 text-emerald-200'
              : 'bg-rose-500/10 text-rose-200'
          }`}
        >
          {state.message}
        </div>
      )}
    </form>
  )
}
