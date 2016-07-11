package alexsander.com.br.friendsintown;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

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
}
