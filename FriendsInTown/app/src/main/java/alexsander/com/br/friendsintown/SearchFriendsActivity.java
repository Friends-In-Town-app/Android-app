package alexsander.com.br.friendsintown;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class SearchFriendsActivity extends AppCompatActivity {

    private String token;
    private TextView tvFriend;
    private Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handleIntent(getIntent());

        tvFriend = (TextView) findViewById(R.id.tv_friend_name);
        btnAdd = (Button) findViewById(R.id.btn_add_friend);

        token = UserService.getToken(this);

    }

    private class GetFriendTask extends AsyncTask<String, Void, Friend> {

        @Override
        protected Friend doInBackground(String... strings) {
            return UserService.searchFriend(strings[0]);
        }

        @Override
        protected void onPostExecute(final Friend foundFriend) {
            super.onPostExecute(foundFriend);
            if (foundFriend.getName() != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvFriend.setText("Do you want to add " + foundFriend.getName() + " to your friend's list?");
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AddFriendTask task = new AddFriendTask();
                                task.execute(token, foundFriend.getId());
                                finish();
                            }
                        });
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvFriend.setText("Could not find any friend with this email");
                        btnAdd.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }

    private class AddFriendTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            UserService.addFriend(strings[0], strings[1]);
            return null;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("Query", query);
            GetFriendTask task = new GetFriendTask();
            task.execute(query);
        }
    }
}
