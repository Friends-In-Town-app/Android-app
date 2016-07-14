package alexsander.com.br.friendsintown;

/**
 * Created by 212571132 on 7/14/16.
 */
public class Friend {
    private String name;
    private String location;
    private float distance;

    public Friend(String name, String location, float distance) {
        this.name = name;
        this.location = location;
        this.distance = distance;
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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
