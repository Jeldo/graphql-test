const testResolvers = {
  Query: {
    testQuery: async () => {
      return 'Result of Test Query';
    },
  },
};

module.exports = {
  testResolvers
};