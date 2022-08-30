import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite';
import { VantResolver } from 'unplugin-vue-components/resolvers';
import { resolve } from 'path'


// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),

    Components({
      resolvers: [VantResolver()],
    }),
  ],
  build:{
    target:['edge90','chrome90','firefox90','safari15']
  },
  resolve: {
    // 配置路径别名
    alias: {
      '@': resolve(__dirname, './src'),
    },
  },
  server: {
    proxy: {
      // 字符串简写写法
      '/api': {
        target: 'http://localhost:8080/',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      },
    }
  },

})
