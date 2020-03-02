const express = require('express');
const { ApolloServer } = require('apollo-server-express');
const cors = require('cors');
const models = require('./models');
const { typeDefs } = require('./typeDefs');
const { resolvers } = require('./resolvers');
const config = require('./config');

const app = express();

app.use(cors());

app.get('/', (req, res) => {
  console.log(req.headers);
  res.json('index');
});

app.post('/', (req, res) => {
  console.log(req.headers);
  res.json('index');
});

const server = new ApolloServer({ typeDefs, resolvers });
server.applyMiddleware({
  app: app,
  path: '/graphql',
});

models.connectDB().then(async () => {
  app.listen({ port: config.PORT }, () =>
    console.log(`🚀 Server ready at http://localhost:${config.PORT}${server.graphqlPath}`)
  );
});