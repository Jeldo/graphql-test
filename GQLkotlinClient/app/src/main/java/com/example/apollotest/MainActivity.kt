package com.example.apollotest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.rxMutate
import com.apollographql.apollo.rx2.rxQuery
import com.apollographql.sample.AddUserMutation
import com.apollographql.sample.DeleteUserMutation
import com.apollographql.sample.GetUsersQuery
import com.apollographql.sample.TestQuery
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient


class MainActivity : AppCompatActivity() {
    private val baseURL = "http://192.168.0.40:8100/graphql"

    private fun getApolloClient(): ApolloClient {
        val okHttpClient = OkHttpClient.Builder().build()
        return ApolloClient.builder().serverUrl(baseURL)
            .okHttpClient(okHttpClient).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apolloClient = getApolloClient()
        sendTestQuery(apolloClient)
        addUser(apolloClient)
        getUsers(apolloClient)
        deleteUser(apolloClient,"5e5e23effc1dcf138fa6a8d8")
    }


    @SuppressLint("CheckResult")
    private fun getUsers(apolloClient: ApolloClient) {
        val getUsersQuery: GetUsersQuery = GetUsersQuery.builder().build()
        apolloClient.rxQuery(getUsersQuery)
            .subscribe(
                {
                    if (it.data()!!.users!!.size == 0) {
                        print("No user in database")
                    } else {
                        val userList: List<GetUsersQuery.GetUser> =
                            it.data()!!.users as List<GetUsersQuery.GetUser>
                        for (i in 0..userList.size) {
                            println("ID: " + userList[i].id())
                            println("Name: " + userList[i].userName())
                            println("Age: " + userList[i].userAge())

                        }
                    }
                }, { Log.e("ERR", it.message.toString()) }) //콜백으로 구독중
    }

    @SuppressLint("CheckResult")
    private fun sendTestQuery(getApolloClient: ApolloClient) {
        Log.e("TestQuery", "start")
        val testQuery: TestQuery = TestQuery.builder().build()
        getApolloClient.rxQuery(testQuery) //발행
            .subscribeOn(Schedulers.io()) // 처리를 어디서할지
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.e("Response", it.data().toString())
                }, { Log.e("ERR", it.message.toString()) }) //콜백으로 구독중
    }

    @SuppressLint("CheckResult")
    private fun addUser(getApolloClient: ApolloClient) {
        Log.e("AddQuery", "start")
        val userName: String = "Florida곽"
        val userAge: String = "30"
        val addUserMutation: AddUserMutation = AddUserMutation.builder().userName(userName)
            .userAge(userAge.toInt()).build()

        getApolloClient.rxMutate(addUserMutation).subscribeOn(Schedulers.io()).subscribe(
            {
                Log.e("Response", it.data().toString())
            }, { Log.e("ERR", it.message.toString()) })
    }

    @SuppressLint("CheckResult")
    private fun deleteUser(apolloClient: ApolloClient, id: String) {
        val deleteUserMutation: DeleteUserMutation = DeleteUserMutation.builder().id(id).build()

        apolloClient.rxMutate(deleteUserMutation).subscribe({
            println("RES: " + it.data()!!.deleteUser())
        },{
            Log.e("NETWORK_ERR", it.toString())
        })
    }

}
