package alexsander.com.br.friendsintown;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by 212571132 on 7/14/16.
 */
public class Friend {
    private String id;
    private String friendRequestID;
    private String name;
    private String location;
    private LatLng latLng;
    private float distance;

    public Friend(String id, String name, String location, float distance) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.distance = distance;
    }

    public Friend(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriendRequestID() {
        return friendRequestID;
    }

    public void setFriendRequestID(String friendRequestID) {
        this.friendRequestID = friendRequestID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
