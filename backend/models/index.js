const mongoose = require('mongoose');
const { User } = require('./user');

const connectDB = () => {
  console.log("Connected with MongoDB")
  return mongoose.connect('mongodb://localhost/graphqldb', { useNewUrlParser: true });
};

module.exports = {
  connectDB,
  User,
};
