const { gql } = require('apollo-server-express');

const query = gql`
  type Query {
    getUsers: [User],
    getUserById(id: ID!): User,
    testQuery: String,
  }
`

module.exports = {
  query,
};