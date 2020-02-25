const express = require('express');
const { ApolloServer } = require('apollo-server-express');
const models = require('./models');
const { typeDefs } = require('./typeDefs');
const { resolvers } = require('./resolvers');
const config = require('./config');

const server = new ApolloServer({ typeDefs, resolvers });
const app = express();

server.applyMiddleware({ app: app, path: '/graphql' });
models.connectDB().then(async () => {
  app.listen({ port: config.PORT }, () =>
    console.log(`🚀 Server ready at http://localhost:${config.PORT}${server.graphqlPath}`)
  );
});