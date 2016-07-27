package alexsander.com.br.friendsintown;



import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private MapView mapView;
    private GoogleMap map;
    private String token;
    private LatLng myLatLng;
    private Marker myMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        token = UserService.getToken(getContext());

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        ((LocationListenersRegistry)getActivity()).addLocationListener(this);
        new GetFriendsListTask().execute(token);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        ((LocationListenersRegistry)getActivity()).removeLocationListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onLocationAvailable(LatLng latLng) {
        if (myMarker != null) {
            myMarker.remove();
        }
        myMarker = addMarker(map, "Me", latLng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        map.moveCamera(update);
    }

    private class GetFriendsListTask extends AsyncTask<String, Void, List<Friend>> {

        @Override
        protected List<Friend> doInBackground(String... strings) {
            myLatLng = UserService.getUserLocation(strings[0]);
            return UserService.getListFriends(strings[0], myLatLng);
        }

        @Override
        protected void onPostExecute(List<Friend> friends) {
            super.onPostExecute(friends);
            if (friends != null) {
                for (Friend friend: friends) {
                    addMarker(map, friend.getName(), friend.getLatLng());
                }
            }
        }


    }

    private Marker addMarker(GoogleMap map, String title, LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title(title);
        return map.addMarker(markerOptions);
    }

}
