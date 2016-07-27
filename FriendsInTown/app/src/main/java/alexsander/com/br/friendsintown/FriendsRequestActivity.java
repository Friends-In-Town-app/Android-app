package alexsander.com.br.friendsintown;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;

public class FriendsRequestActivity extends AppCompatActivity {
    private String token;
    private FriendListAdapter adapter;
    private RecyclerView recyclerView;
    private List<Friend> friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        token = UserService.getToken(this);

        recyclerView = (RecyclerView) findViewById(R.id.rv_friends);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GetFriendsListTask task = new GetFriendsListTask();
        task.execute(token);
    }

    private FriendListAdapter.OnFriendClickListener onFriendClick () {
        return new FriendListAdapter.OnFriendClickListener() {
            @Override
            public void onFriendClick(View view, final int idx) {
                final AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(FriendsRequestActivity.this)
                        .setMessage("Would you like to add " + friendList.get(idx).getName() + " to your friend's list?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("Request ID", friendList.get(idx).getFriendRequestID());
                                new AcceptFriendRequestTask().execute(token, friendList.get(idx).getFriendRequestID());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                dialog = builder.create();
                dialog.show();
            }
        };
    }

    private class GetFriendsListTask extends AsyncTask<String, Void, List<Friend>> {

        @Override
        protected List<Friend> doInBackground(String... strings) {
            return UserService.getFriendsRequest(token);
        }

        @Override
        protected void onPostExecute(List<Friend> foundFriends) {
            super.onPostExecute(foundFriends);
            if (foundFriends != null) {
                adapter = new FriendListAdapter(foundFriends, FriendsRequestActivity.this, onFriendClick());
                recyclerView.setAdapter(adapter);
            } else {
                Log.d("Get Friends List Task", "Friend's list is null");
            }

            friendList = foundFriends;
        }
    }

    private class AcceptFriendRequestTask extends AsyncTask<String, Void, Friend> {

        @Override
        protected Friend doInBackground(String... strings) {
            UserService.acceptRequest(strings[0], strings[1]);
            return null;
        }
    }
}
