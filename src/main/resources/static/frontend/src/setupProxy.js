// setupProxy.js
const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: 'http://localhost:8443',
      changeOrigin: true,
      secure: false,
      // websocket se serve:
      ws: true,
      // Mantieni il path /api
      pathRewrite: {
        '^/api': '/api'
      }
    })
  );
};
