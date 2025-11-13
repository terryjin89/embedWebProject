import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api/exchange': {
        target: 'https://oapi.koreaexim.go.kr',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/exchange/, '/site/program/financial/exchangeJSON'),
      },
    },
  },
})
