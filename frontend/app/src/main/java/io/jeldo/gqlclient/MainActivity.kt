package io.jeldo.gqlclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val BASE_URL = "http://localhost:8100/graphql"
//        val okHttpClient = OkHttpClient.Builder().build()

        fun provideApolloClient(): ApolloClient {
            return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(OkHttpClient().newBuilder().build())
                .build()
        }

        val apolloClient = provideApolloClient()
        apolloClient.query(
        )

        setContentView(R.layout.activity_main)
    }
}
