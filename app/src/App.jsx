import { useState } from 'react'
import { Tab } from '@headlessui/react'
import { ShieldCheckIcon, UserPlusIcon } from '@heroicons/react/24/solid'
import LoginForm from './components/LoginForm'
import RegistrationForm from './components/RegistrationForm'
import SecurityHighlights from './components/SecurityHighlights'

const tabs = [
  { name: 'Sign in', icon: ShieldCheckIcon, content: <LoginForm /> },
  { name: 'Register', icon: UserPlusIcon, content: <RegistrationForm /> }
]

export default function App() {
  const [selectedIndex, setSelectedIndex] = useState(0)

  return (
    <div className="min-h-screen gradient-bg">
      <header className="max-w-5xl mx-auto px-6 py-12">
        <div className="flex flex-col gap-3 text-left">
          <p className="text-sm tracking-[0.35em] uppercase text-teal-200">KuberWallet Identity</p>
          <h1 className="text-4xl md:text-5xl font-semibold text-white">
            Secure access to your digital wealth ecosystem
          </h1>
          <p className="text-lg text-slate-300 max-w-2xl">
            Enterprise-grade identity and KYC orchestration, multi-factor authentication, and comprehensive
            consent management in a single, elegant experience.
          </p>
        </div>
      </header>

      <main className="max-w-5xl mx-auto px-6 pb-16 grid md:grid-cols-[2fr,1.2fr] gap-10 items-start">
        <section className="bg-slate-900/80 backdrop-blur rounded-3xl border border-white/10 shadow-xl overflow-hidden">
          <Tab.Group selectedIndex={selectedIndex} onChange={setSelectedIndex}>
            <Tab.List className="grid grid-cols-2">
              {tabs.map((tab) => (
                <Tab key={tab.name} className="focus:outline-none">
                  {({ selected }) => (
                    <div
                      className={`flex items-center justify-center gap-2 px-6 py-4 text-sm font-semibold transition ${
                        selected
                          ? 'bg-slate-800/80 text-white'
                          : 'bg-slate-900/70 text-slate-400 hover:text-white'
                      }`}
                    >
                      <tab.icon className="h-5 w-5" />
                      {tab.name}
                    </div>
                  )}
                </Tab>
              ))}
            </Tab.List>
            <Tab.Panels className="p-8">
              {tabs.map((tab) => (
                <Tab.Panel key={tab.name}>{tab.content}</Tab.Panel>
              ))}
            </Tab.Panels>
          </Tab.Group>
        </section>
        <aside>
          <SecurityHighlights />
        </aside>
      </main>
    </div>
  )
}
