package alexsander.com.br.friendsintown;

import android.app.Activity;
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
    private static final String
            URL_REGISTER_USER = "http://64.137.233.224:3000/createaccountemail/";

    private static final String
            URL_CHECK_USER = "http://64.137.233.224:3000/checkemailexistence/";

    private static final String
            URL_LOGIN_USER = "http://64.137.233.224:3000/loginemail/";

    private static final String
            URL_SEND_LOCATION_USER = "http://64.137.233.224:3000/location/";
    private static final String
            URL_LIST_FRIENDS = "http://64.137.233.224:3000/listfriends/";

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

    public static boolean userLogin (Activity activity, String email, String password) {
        if (checkUserExists(email)) {
            HttpRequest request = HttpRequest.get(URL_LOGIN_USER + email + "/" + password + "/android");
            if (request.code() == 200) {
                try {
                    JSONObject response = new JSONObject(request.body());
                    boolean success = response.getBoolean("success");
                    if (!response.getString("token").isEmpty()) {
                        SharedPreferences pref = activity.getPreferences(Context.MODE_PRIVATE);
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

    public static boolean sendUserLocation(String token, LatLng latLng, String town, String country) {
        HttpRequest request = HttpRequest.post(URL_SEND_LOCATION_USER + token + "/" + latLng.latitude +
                                                "/" + latLng.longitude + "/" + town + "/" + country);
        if (request.code() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public static List<Friend> getListFriends(String token, LatLng userLocation) {
        List<Friend> friendsList = new ArrayList<>();
        HttpRequest request = HttpRequest.get(URL_LIST_FRIENDS + token);
        if (request.code() == 200) {
            try {
                JSONObject root = new JSONObject(request.body());
                JSONArray friends = root.getJSONArray("friends");
                for (int i = 0; i < friends.length(); i++) {
                    float[] distance = new float[1];
                    JSONObject friendObject = friends.getJSONObject(i);
                    String name = friendObject.getString("n");
                    String location = friendObject.getString("city") + ", " + friendObject.getString("ct");
                    LatLng friendLatLng = new LatLng(friendObject.getJSONArray("pos").getDouble(0),
                                                    friendObject.getJSONArray("pos").getDouble(1));

                    if (userLocation != null) {
                        Location.distanceBetween(userLocation.latitude, userLocation.longitude, friendLatLng.latitude, friendLatLng.longitude, distance);
                    }
                    Friend friend = new Friend(name, location, distance[0]);
                    friendsList.add(friend);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return friendsList;
    }
}
