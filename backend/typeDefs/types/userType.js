const { gql } = require('apollo-server-express');

const userType = gql`
  type User {
    id: ID!,
    userName: String!,
    userAge: Int!,
  }
`;

module.exports = {
  userType,
};
