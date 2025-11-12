import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useState } from 'react'
import axios from 'axios'
import { registrationSchema } from '../lib/validation'

const initialState = {
  status: 'idle',
  message: null
}

export default function RegistrationForm() {
  const [state, setState] = useState(initialState)
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting }
  } = useForm({ resolver: zodResolver(registrationSchema) })

  const onSubmit = async (data) => {
    setState({ status: 'loading', message: null })
    try {
      await axios.post('/api/auth/register', data)
      setState({ status: 'success', message: 'Registration successful. Please sign in with your new credentials.' })
      reset()
    } catch (error) {
      const message = error.response?.data?.message ?? 'Registration failed. Please review your information.'
      setState({ status: 'error', message })
    }
  }

  return (
    <form className="space-y-5" onSubmit={handleSubmit(onSubmit)}>
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
        <div>
          <label htmlFor="firstName" className="block text-sm font-medium text-slate-200">
            First name
          </label>
          <input
            id="firstName"
            type="text"
            autoComplete="given-name"
            className="mt-2 w-full rounded-xl border border-white/10 bg-slate-950/80 px-4 py-3 text-white focus:border-teal-400 focus:outline-none"
            {...register('firstName')}
          />
          {errors.firstName && <p className="mt-1 text-sm text-rose-400">{errors.firstName.message}</p>}
        </div>
        <div>
          <label htmlFor="lastName" className="block text-sm font-medium text-slate-200">
            Last name
          </label>
          <input
            id="lastName"
            type="text"
            autoComplete="family-name"
            className="mt-2 w-full rounded-xl border border-white/10 bg-slate-950/80 px-4 py-3 text-white focus:border-teal-400 focus:outline-none"
            {...register('lastName')}
          />
          {errors.lastName && <p className="mt-1 text-sm text-rose-400">{errors.lastName.message}</p>}
        </div>
      </div>

      <div>
        <label htmlFor="email" className="block text-sm font-medium text-slate-200">
          Corporate email
        </label>
        <input
          id="email"
          type="email"
          autoComplete="email"
          className="mt-2 w-full rounded-xl border border-white/10 bg-slate-950/80 px-4 py-3 text-white focus:border-teal-400 focus:outline-none"
          {...register('email')}
        />
        {errors.email && <p className="mt-1 text-sm text-rose-400">{errors.email.message}</p>}
      </div>

      <div>
        <label htmlFor="username" className="block text-sm font-medium text-slate-200">
          Username
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
          autoComplete="new-password"
          className="mt-2 w-full rounded-xl border border-white/10 bg-slate-950/80 px-4 py-3 text-white focus:border-teal-400 focus:outline-none"
          {...register('password')}
        />
        {errors.password && <p className="mt-1 text-sm text-rose-400">{errors.password.message}</p>}
      </div>

      <div className="flex items-start gap-3">
        <input
          id="termsAccepted"
          type="checkbox"
          className="mt-1 h-4 w-4 rounded border border-white/20 bg-slate-950/80 text-primary focus:ring-primary"
          {...register('termsAccepted')}
        />
        <label htmlFor="termsAccepted" className="text-sm text-slate-300">
          I consent to the processing of my personal data for the purposes of identity verification, regulatory
          compliance, and secure access to KuberWallet services.
        </label>
      </div>
      {errors.termsAccepted && <p className="text-sm text-rose-400">{errors.termsAccepted.message}</p>}

      <button
        type="submit"
        className="w-full rounded-xl bg-accent px-4 py-3 text-sm font-semibold text-white shadow-lg shadow-accent/30 transition hover:bg-accent/90"
        disabled={isSubmitting}
      >
        {isSubmitting ? 'Creating secure identity…' : 'Create enterprise identity'}
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
