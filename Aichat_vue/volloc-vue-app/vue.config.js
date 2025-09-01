const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
   devServer: {
    port: 1081 // 在这里指定您想要的端口号
  }
})
