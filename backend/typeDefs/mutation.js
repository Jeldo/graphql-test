const { gql } = require('apollo-server-express');

const mutation = gql`
  type Mutation {
    addUser(userName: String!, userAge:Int!): User,
    deleteUser(id: ID!): User,
  }
`

module.exports = {
  mutation
}
