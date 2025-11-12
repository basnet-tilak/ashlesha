const highlights = [
  {
    title: 'Adaptive multi-factor authentication',
    description:
      'Configurable MFA policies across TOTP, FIDO2, and recovery codes with adaptive risk scoring and session insights.'
  },
  {
    title: 'Granular consent orchestration',
    description:
      'Capture, track, and audit customer consent lifecycles with immutable ledgers and jurisdiction-aware templates.'
  },
  {
    title: 'KYC intelligence hub',
    description:
      'Streamlined document review, third-party provider integrations, and proactive expiry notifications for compliance teams.'
  }
]

export default function SecurityHighlights() {
  return (
    <section className="space-y-6 rounded-3xl border border-white/10 bg-slate-900/70 p-8 shadow-lg">
      <h2 className="text-xl font-semibold text-white">Operational-grade safeguards</h2>
      <p className="text-sm text-slate-300">
        KuberWallet unifies identity, consent, and compliance workflows with real-time observability and automated
        governance.
      </p>
      <div className="space-y-4">
        {highlights.map((item) => (
          <div key={item.title} className="rounded-2xl border border-white/5 bg-slate-950/60 p-4">
            <h3 className="text-sm font-semibold text-teal-200">{item.title}</h3>
            <p className="mt-1 text-sm text-slate-400">{item.description}</p>
          </div>
        ))}
      </div>
    </section>
  )
}
