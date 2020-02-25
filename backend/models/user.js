const mongoose = require('mongoose');
const { Schema } = mongoose;

const userSchema = new Schema({
  userName: { type: String, required: true },
  userAge: { type: Number, required: true },
});

const User = mongoose.model('user', userSchema);

module.exports = {
  User
};