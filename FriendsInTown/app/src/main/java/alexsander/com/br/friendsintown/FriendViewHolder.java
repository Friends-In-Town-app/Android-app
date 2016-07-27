package alexsander.com.br.friendsintown;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by 212571132 on 7/14/16.
 */
public class FriendViewHolder extends RecyclerView.ViewHolder {
    public TextView tvFriendName;
    public TextView tvFriendLocation;
    public TextView tvFriendDistance;

    public FriendViewHolder(View itemView) {
        super(itemView);
        tvFriendName = (TextView) itemView.findViewById(R.id.friend_name);
        tvFriendLocation = (TextView) itemView.findViewById(R.id.friend_location);
        tvFriendDistance = (TextView) itemView.findViewById(R.id.friend_distance);
    }
}
