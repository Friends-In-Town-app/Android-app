package alexsander.com.br.friendsintown;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by 212571132 on 7/26/16.
 */
public class LocationIntentService extends IntentService {
    public LocationIntentService() {
        super("worker_thread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Location location = intent.getParcelableExtra(FriendsInTownActivity.LOCATION);
        List<Address> addressList = new ArrayList<Address>();
        String error = "";
        String resultAddress = null;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addressList = (ArrayList<Address>) geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            error = "Network problem";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            error = "Illegal arguments.";
        }

        if (addressList != null && !addressList.isEmpty()) {
            Address address = addressList.get(0);

            for (int i = 0, tam = address.getMaxAddressLineIndex(); i < tam; i++) {
                if (address.getAddressLine(i) != null) {
                    resultAddress += address.getAddressLine(i);
                    resultAddress += i < tam - 1 ? ", " : "";
                }
            }
        } else {
            resultAddress = error;
        }

        MessageEB messageEB = new MessageEB();
        messageEB.setResultMessage(resultAddress);

        EventBus.getDefault().post(messageEB);
    }
}
