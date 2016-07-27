package alexsander.com.br.friendsintown;

/**
 * Created by 212571132 on 7/26/16.
 */
public interface LocationListenersRegistry {
    public void addLocationListener(LocationListener listener);
    public void removeLocationListener(LocationListener listener);
}
