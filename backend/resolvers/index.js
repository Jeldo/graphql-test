const { userResolvers } = require('./userResolvers');
const { testResolvers } = require('./testResolvers');

const resolvers = [userResolvers, testResolvers,];

module.exports = {
  resolvers,
};