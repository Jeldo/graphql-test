const mongoose = require('mongoose');
mongoose.Promise = global.Promise;

const url = 'mongodb://localhost:27017/testgraphql';

mongoose.connect(url, { useNewUrlParser: true, useUnifiedTopology: true });
mongoose.connection.once('open', () => console.log(`Connected to mongo at ${url}`));

const PORT = 8100;

module.exports = {
  PORT,
};