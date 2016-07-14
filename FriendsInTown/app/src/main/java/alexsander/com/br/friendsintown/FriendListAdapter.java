package alexsander.com.br.friendsintown;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 212571132 on 7/14/16.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendViewHolder>{
    private List<Friend> friends;
    private Context context;

    public FriendListAdapter(List<Friend> friends, Context context) {
        this.friends = friends;
        this.context = context;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_info, parent, false);
        FriendViewHolder holder = new FriendViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        Friend friend = friends.get(position);

        holder.tvFriendName.setText(friend.getName());
        holder.tvFriendLocation.setText(friend.getLocation());
        holder.tvFriendDistance.setText(friend.getDistance() + "m");
    }

    @Override
    public int getItemCount() {
        return friends != null ? friends.size() : 0;
    }
}
