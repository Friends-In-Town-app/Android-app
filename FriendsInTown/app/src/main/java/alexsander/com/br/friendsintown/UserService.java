package alexsander.com.br.friendsintown;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex1_000 on 30/06/2016.
 */
public class UserService {

    private static final String BASE_URL = "http://64.137.233.224:3000/";
    private static final String
            URL_REGISTER_USER = BASE_URL + "createaccountemail/";

    private static final String
            URL_CHECK_USER = BASE_URL + "checkemailexistence/";

    private static final String
            URL_LOGIN_USER = BASE_URL + "loginemail/";

    private static final String
            URL_LOCATION_USER = BASE_URL + "location/";
    private static final String
            URL_LIST_FRIENDS = BASE_URL + "listfriends/";

    private static final String URL_SEE_FRIENDS_REQUEST = BASE_URL + "pendingrequests/";
    private static final String URL_ACCEPT_FRIEND_REQUEST = BASE_URL + "acceptfriend/";
    private static final String URL_SEARCH_FRIEND = BASE_URL + "search/";
    private static final String URL_ADD_FRIEND = BASE_URL + "requestfriendship/";

    public static String getToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = pref.getString("token", "");
        return token;
    }

    public static boolean registerUser(String name, String email, String password) {
        if (!checkUserExists(email)) {
            HttpRequest request = HttpRequest.post(URL_REGISTER_USER + email + "/" + password + "/" + name);
            if (request.code() == 200) {
                try {
                    JSONObject response = new JSONObject(request.body());
                    Log.d("Register", response.toString());
                    boolean success = response.getBoolean("success");
                    return success;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    private static boolean checkUserExists(String email) {
        HttpRequest request = HttpRequest.get(URL_CHECK_USER + email)
                .contentType("application/json");

        if (request.code() == 200) {
            try {
                JSONObject response = new JSONObject(request.body());
                Log.d("Check Email", response.toString());
                boolean success = response.getBoolean("success");
                return success;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean userLogin (Context context, String email, String password) {
        if (checkUserExists(email)) {
            HttpRequest request = HttpRequest.post(URL_LOGIN_USER + email + "/" + password + "/android");
            if (request.code() == 200) {
                try {
                    JSONObject response = new JSONObject(request.body());
                    Log.d("Login", response.toString());
                    boolean success = response.getBoolean("success");
                    if (!response.getString("token").isEmpty()) {
                        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("token", response.getString("token"));
                        editor.commit();
                    }
                    Log.d("Token Login", response.getString("token"));
                    return success;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public static void sendUserLocation(String token, LatLng latLng, String address) {
        HttpRequest request = HttpRequest.post(URL_LOCATION_USER + token + "/" + latLng.latitude +
                                                "/" + latLng.longitude + "/" + address.replace(" ", "%20").replace("null", ""));
        Log.d("sendUserLocation", request.body());
    }

    public static LatLng getUserLocation(String token) {
        HttpRequest request = HttpRequest.get(URL_LOCATION_USER + token);
        LatLng latLng = null;
        if (request.code() == 200) {
            try {
                JSONObject root = new JSONObject(request.body());
                JSONObject user = root.getJSONObject("user");
                JSONArray pos = user.getJSONArray("pos");
                latLng = new LatLng(pos.getDouble(0), pos.getDouble(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return latLng;
    }

    public static List<Friend> getListFriends(String token, LatLng userLocation) {
        List<Friend> friendsList = new ArrayList<>();
        HttpRequest request = HttpRequest.get(URL_LIST_FRIENDS + token);
        if (request.code() == 200) {
            try {
                JSONObject root = new JSONObject(request.body());
                JSONArray friends = root.getJSONArray("friends");
                Log.d("Get friend's list", friends.toString());
                for (int i = 0; i < friends.length(); i++) {
                    float[] distance = new float[1];
                    JSONObject friendObject = friends.getJSONObject(i).getJSONObject("user");
                    String id = friendObject.getString("_id");
                    String name = friendObject.getString("n");
                    String location = friendObject.getString("add");
                    LatLng friendLatLng = new LatLng(friendObject.getJSONArray("pos").getDouble(0),
                            friendObject.getJSONArray("pos").getDouble(1));

                    if (userLocation != null) {
                        Location.distanceBetween(userLocation.latitude, userLocation.longitude, friendLatLng.latitude, friendLatLng.longitude, distance);
                        Log.d("Distance", String.valueOf(distance[0]));
                    }

                    Friend friend = new Friend(id, name, "", distance[0]);
                    friend.setLocation(location);
                    friend.setLatLng(friendLatLng);
                    friendsList.add(friend);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return friendsList;
    }

    public static List<Friend> getFriendsRequest(String token) {
        List<Friend> friends = new ArrayList<>();
        HttpRequest request = HttpRequest.get(URL_SEE_FRIENDS_REQUEST + token);
        if (request.code() == 200) {
            try {
                JSONObject root = new JSONObject(request.body());
                Log.d("Get Friend's Request", root.toString());
                JSONArray requests = root.getJSONArray("requests");
                for (int i = 0; i < requests.length(); i++) {
                    JSONObject friendUser = requests.getJSONObject(i).getJSONObject("user");
                    String id = friendUser.getString("_id");
                    String name = friendUser.getString("n");

                    Friend friend = new Friend(id, name, "", 0);
                    friend.setFriendRequestID(requests.getJSONObject(i).getString("_id"));
                    friends.add(friend);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return friends;
    }

    public static boolean acceptRequest(String token, String requestID) {
        HttpRequest request = HttpRequest.post(URL_ACCEPT_FRIEND_REQUEST + token + "/" + requestID);
        Log.d("Accept Request", request.body());
        if (request.code() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public static Friend searchFriend(String email) {
        Friend friend = new Friend();
        HttpRequest request = HttpRequest.get(URL_SEARCH_FRIEND + email);
        if (request.code() == 200) {
            try {
                JSONObject user = new JSONObject(request.body()).getJSONObject("user");
                Log.d("Search friend", user.toString());
                String id = user.getString("_id");
                String name = user.getString("n");
                friend.setId(id);
                friend.setName(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return friend;
    }

    public static void addFriend(String token, String friendID) {
        HttpRequest request = HttpRequest.post(URL_ADD_FRIEND + token + "/" + friendID);
        if (request.code() == 200) {
            Log.d("Add Friend", request.body());
        } else {
            Log.d("Add Friend", "Fail");
        }
    }
}
