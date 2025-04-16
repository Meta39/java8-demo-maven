// 如果改变就需要重启项目
module.exports = {
    devServer: {
        open: true,           // 是否自动打开项目
        host: '0.0.0.0',      // 制定域名
        port: 8081,           // 端口号
        https: false,         // 把访问变成https
        hotOnly: false,       // 热更新
        disableHostCheck: true
    }
}