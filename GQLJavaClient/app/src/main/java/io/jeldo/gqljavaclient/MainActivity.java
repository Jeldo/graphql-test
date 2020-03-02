package io.jeldo.gqljavaclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private Context currentContext = this;
    private EditText etUserName;
    private EditText etUserAge;
    private EditText etUserId;

    /* for real device */
    private static final String BASE_URL = "http://192.168.0.24:8100/graphql";
    /* for emulator */
    // private static final String BASE_URL = "http://10.0.2.2:8100/graphql";

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            // .addInterceptor(new Interceptor() {
            // @Override
            // public okhttp3.Response intercept(Chain chain) throws IOException {
            // Request original = chain.request();
            // Request request = original.newBuilder()
            // .header("Authorization", "Bearer 5ea055c259d307c46b9f7857101541a66aee7743")
            // .method(original.method(), original.body())
            // .build();
            // return chain.proceed(request);
            // }
            // })
            .build();

    ApolloClient apolloClient = ApolloClient.builder().serverUrl(BASE_URL).okHttpClient(okHttpClient).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnTestQuery = findViewById(R.id.btnTestQuery);
        Button btnGetUsers = findViewById(R.id.btnGetUsers);
        Button btnAddUser = findViewById(R.id.btnAddUser);
        Button btnDeleteUser = findViewById(R.id.btnDeleteUser);
        etUserName = findViewById(R.id.etUserName);
        etUserAge = findViewById(R.id.etUserAge);
        etUserId = findViewById(R.id.etUserId);

        btnTestQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTestQuery();
            }
        });
        btnGetUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUsers();
            }
        });
        btnAddUser.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        }));
        btnDeleteUser.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        }));
    }

    private void sendTestQuery() {
        System.out.println("TEST QUERY START");
        TestQuery testQuery = TestQuery.builder().build();
        System.out.println(testQuery.toString());

        apolloClient.query(testQuery).enqueue(new ApolloCall.Callback<TestQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<TestQuery.Data> response) {
                System.out.println("RES: " + response.data().testQuery);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("NETWORK_ERR", e.toString());
                Log.getStackTraceString(e);
            }
        });
    }

    private void getUsers() {
        System.out.println("GET USER START");
        GetUsersQuery getUsersQuery = GetUsersQuery.builder().build();

        apolloClient.query(getUsersQuery).enqueue(new ApolloCall.Callback<GetUsersQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetUsersQuery.Data> response) {
                if (response.data().getUsers.size() == 0) {
                    System.out.println("No user in database");
                } else {
                    List<GetUsersQuery.GetUser> userList = response.data().getUsers;
                    for (int i = 0; i < userList.size(); ++i) {
                        System.out.println("ID: " + userList.get(i).id);
                        System.out.println("Name: " + userList.get(i).userName);
                        System.out.println("Age: " + userList.get(i).userAge);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("NETWORK_ERR@GETUSERS", e.toString());
                Log.getStackTraceString(e);
            }
        });
    }

    public void addUser() {
        System.out.println("ADD USER START");
        if (!isValidAddUserForm()) {
            Toast.makeText(getBaseContext(), "Input User info", Toast.LENGTH_SHORT).show();
            return;
        }

        String userName = etUserName.getText().toString();
        String userAge = etUserAge.getText().toString();

        AddUserMutation addUserMutation = AddUserMutation.builder().userName(userName)
                .userAge(Integer.parseInt(userAge)).build();

        apolloClient.mutate(addUserMutation).enqueue(new ApolloCall.Callback<AddUserMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<AddUserMutation.Data> response) {
                System.out.println("RES: " + response.data());
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("NETWORK_ERR", e.toString());
                Log.getStackTraceString(e);
            }
        });
    }

    public void deleteUser() {
        System.out.println("DELETE USER START");
        if (!isValidDeleteUserForm()) {
            Toast.makeText(getBaseContext(), "Input User Id", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = etUserId.getText().toString();
        DeleteUserMutation deleteUserMutation = DeleteUserMutation.builder().id(userId).build();

        apolloClient.mutate(deleteUserMutation).enqueue(new ApolloCall.Callback<DeleteUserMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<DeleteUserMutation.Data> response) {
                if (response.data().deleteUser == null) {
                    // due to wrong ID
                    System.out.println("Null response@DeleteUser");
                } else {
                    System.out.println("RES: " + response.data().deleteUser);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("NETWORK_ERR", e.toString());
                Log.getStackTraceString(e);
            }
        });
    }

    public boolean isValidAddUserForm() {
        boolean isValid = true;
        String userName = etUserName.getText().toString();
        String userAge = etUserAge.getText().toString();

        if (userName.isEmpty()) {
            isValid = false;
        }
        if (userAge.isEmpty()) {
            isValid = false;
        }
        return isValid;
    }

    public boolean isValidDeleteUserForm() {
        boolean isValid = true;
        String userId = etUserId.getText().toString();
        if (userId.isEmpty()) {
            isValid = false;
        }
        return isValid;
    }
}
