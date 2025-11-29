import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true, // Docker 컨테이너 외부에서 접근 허용
    watch: {
      usePolling: true, // Docker 환경에서 HMR을 위한 폴링 활성화
    },
    proxy: {
      // 백엔드 API 프록시 설정
      '/api': {
        target: process.env.VITE_PROXY_TARGET || 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        // /api/** 경로는 백엔드로 프록시
      },
    },
  },
})
