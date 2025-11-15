import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // 백엔드 API 프록시 설정
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        // /api/** 경로는 백엔드로 프록시
        // 단, /api/exchange는 아래 설정으로 덮어쓰기
      },
      // 환율 API는 외부 API로 직접 프록시
      '/api/exchange': {
        target: 'https://oapi.koreaexim.go.kr',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/exchange/, '/site/program/financial/exchangeJSON'),
      },
    },
  },
})
