var path = require('path');

module.exports = {
	entry: './src/main/js/App.js',
	output: {
		path: __dirname,
        filename: './src/main/resources/static/built/bundle.js'
	},
	module:{
		rules: [
			{
				test: /\.(js|jsx)$/,
	            exclude: /node_modules/,
	            use: {
	              loader: "babel-loader"
	            }
	        },
	        { test: /\.css$/, loader: 'style-loader!css-loader'}
		]
	}
};
