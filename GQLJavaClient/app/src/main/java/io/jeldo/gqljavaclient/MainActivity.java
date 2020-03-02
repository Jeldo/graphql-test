package io.jeldo.gqljavaclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://api.github.com/graphql";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Authorization","Bearer 5ea055c259d307c46b9f7857101541a66aee7743")
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();

        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();


        System.out.println("QUERY START");

        TestQuery testQuery = TestQuery.builder().build();
        System.out.println(testQuery.toString());

        apolloClient.query(testQuery).enqueue(new ApolloCall.Callback<TestQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<TestQuery.Data> response) {
                System.out.println("RESPONSE "+response.data());
//                Log.e("RESPONSE", response.data());
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("NETWORK_ERR", e.toString());
            }
        });

//        AddUserMutation addUserMutation = AddUserMutation.builder()
//                .userName("Kim")
//                .userAge(20)
//                .build();
//
//        apolloClient.mutate(addUserMutation).enqueue(new ApolloCall.Callback<AddUserMutation.Data>() {
//            @Override
//            public void onResponse(@NotNull Response<AddUserMutation.Data> response) {
//                System.out.println("RESPONSE"+response.toString());
//                Log.e("RESPONSE", response.toString());
//            }
//
//            @Override
//            public void onFailure(@NotNull ApolloException e) {
//                Log.e("NETWORK_ERR",e.toString());
//                Log.getStackTraceString(e);
//            }
//        });
    }
}
