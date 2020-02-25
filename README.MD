## How to start

```bash
yarn install
yarn start
```

### API Definition

#### Query

testQuery

```
query{
    testQuery
}
```

getUsers

```
query{
    getUsers(){
        id,
        userName,
        userAge,
    }
}
```

getUserById

```
query{
    getUserById(id){
        id,
        userName,
        userAge
    }
}
```

...
