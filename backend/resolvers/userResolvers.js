const { User } = require('../models'); // import real database

const userResolvers = {
  Query: {
    getUsers: async () => User.find({}),
    getUserById: async (_, { id }) => User.findById(id),
  },
  Mutation: {
    addUser: async (_, args) => {
      let newUser;
      try {
        newUser = new User({
          userName: args.userName,
          userAge: args.userAge,
        });
        return await newUser.save();
      } catch (e) {
        console.log('error at addUser');
        return e.message;
      }
    },
    // args.id == { id }
    deleteUser: async (_, { id }) => {
      try {
        let deletedUser = await User.findOneAndDelete({ _id: id });
        return deletedUser;
      } catch (e) {
        console.log('error at deleteUser');
        return e.message;
      }
    },
  },
};

module.exports = {
  userResolvers
};