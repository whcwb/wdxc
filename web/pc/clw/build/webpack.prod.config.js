const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const cleanWebpackPlugin = require('clean-webpack-plugin');
const UglifyJsParallelPlugin = require('webpack-uglify-parallel');
const merge = require('webpack-merge');
const webpackBaseConfig = require('./webpack.base.config.js');
const os = require('os');
const fs = require('fs');
const path = require('path');
const package = require('../package.json');

// fs.open('./build/env.js', 'w', function(err, fd) {
//     const buf = 'export default "production";';
//     fs.write(fd, buf, 0, buf.length, 0, function(err, written, buffer) {});
// });

/**
 *  用[hash]的话，由于每次使用 webpack 构建代码的时候，此 hash 字符串都会更新，因此相当于强制刷新浏览器缓存。
    用[chunkhash]的话，则会根据具体 chunk 的内容来形成一个 hash 字符串来插入到文件名上；换句说， chunk 的内容不变，
        该 chunk 所对应生成出来的文件的文件名也不会变，由此，浏览器缓存便能得以继续利用。
 */
module.exports = merge(webpackBaseConfig, {
    output: {
        publicPath: './dist/',  // 修改 https://iv...admin 这部分为你的服务器域名
        filename: '[name].[chunkhash].js',
        chunkFilename: '[name].[chunkhash].chunk.js'
    },
    module: {
        rules: [
            {
                test: /\.(woff|svg|eot|ttf)\??.*$/,
                loader: 'file-loader?publicPath=./font/&outputPath=/font/'
            },
        ]
    },
    plugins: [
        new cleanWebpackPlugin(['dist/*'], {
            root: path.resolve(__dirname, '../')
        }),
        new ExtractTextPlugin({
            filename: '[name].[chunkhash].css',
            allChunks: true
        }),
        new webpack.optimize.CommonsChunkPlugin({
            // name: 'vendors',
            // filename: 'vendors.[hash].js'
            name: ['vender-exten', 'vender-base'],
            minChunks: Infinity
        }),
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: '"production"'
            }
        }),
        new webpack.optimize.UglifyJsPlugin({
            compress: {
                warnings: false
            }
        }),
        // new UglifyJsParallelPlugin({
        //     workers: os.cpus().length,
        //     mangle: true,
        //     compressor: {
        //       warnings: false,
        //       drop_console: true,
        //       drop_debugger: true
        //      }
        // }),
        new CopyWebpackPlugin([
            {
                from: 'td_icon.ico'
            },
            {
                from: 'src/styles/fonts',
                to: 'fonts'
//          },
//          {
//              from: 'src/views/main-components/theme-switch/theme'
//          },
//          {
//              from: 'src/views/my-components/text-editor/tinymce'
            }
        ], {
            ignore: [
//              'text-editor.vue'
            ]
        }),
        new HtmlWebpackPlugin({
            title: 'iView admin v' + package.version,
            favicon: './td_icon.ico',
            filename: '../index.html',
            template: '!!ejs-loader!./src/template/index.ejs',
            inject: false
        })
    ]
});
