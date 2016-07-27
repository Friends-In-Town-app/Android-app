package alexsander.com.br.friendsintown;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsListFragment extends Fragment {

    private LatLng userLocation;
    private FriendListAdapter adapter;
    private RecyclerView recyclerView;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);

        token = UserService.getToken(getContext());

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_friends);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        GetFriendsListTask task = new GetFriendsListTask();
        task.execute(token);

        return view;
    }

    private class GetFriendsListTask extends AsyncTask<String, Void, List<Friend>> {

        @Override
        protected List<Friend> doInBackground(String... strings) {
            userLocation = UserService.getUserLocation(strings[0]);
            return UserService.getListFriends(strings[0], userLocation);
        }

        @Override
        protected void onPostExecute(List<Friend> friends) {
            super.onPostExecute(friends);
            if (friends != null) {
                adapter = new FriendListAdapter(friends, getContext(), null);
                recyclerView.setAdapter(adapter);
            }
        }
    }

}
