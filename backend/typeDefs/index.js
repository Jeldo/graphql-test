const { query } = require('./query');
const { mutation } = require('./mutation');
const { userType } = require('./types');

const typeDefs = [query, mutation, userType];

module.exports = {
  typeDefs,
};
