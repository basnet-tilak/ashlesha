import { z } from 'zod'

export const loginSchema = z.object({
  username: z.string().min(3, 'Provide a valid username or email.'),
  password: z.string().min(8, 'Password must contain at least 8 characters.')
})

export const registrationSchema = z
  .object({
    firstName: z.string().min(1, 'First name is required.'),
    lastName: z.string().min(1, 'Last name is required.'),
    email: z.string().email('Enter a valid email address.'),
    username: z.string().min(4, 'Username should be at least 4 characters.'),
    password: z
      .string()
      .min(12, 'Use a minimum of 12 characters for stronger security.')
      .regex(/[A-Z]/, 'Include an uppercase letter.')
      .regex(/[a-z]/, 'Include a lowercase letter.')
      .regex(/[0-9]/, 'Include a digit.')
      .regex(/[!@#$%^&*]/, 'Include a special character (!@#$%^&*).'),
    termsAccepted: z.literal(true, {
      errorMap: () => ({ message: 'Terms and consent acknowledgment is required.' })
    })
  })
  .superRefine((data, ctx) => {
    if (data.password.toLowerCase().includes(data.firstName.toLowerCase())) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        message: 'Avoid using your name inside the password for better security.',
        path: ['password']
      })
    }
  })
