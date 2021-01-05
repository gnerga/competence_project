package model;

public class HotSpot {
    public final String name;
    public final String description;
    public final float longitude;
    public final float latitude;
    public final Type type;

    public HotSpot(String name, String description, float longitude, float latitude, Type type) {
        this.name = name;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "HotSpot{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", type='" + type + '\'' +
                '}';
    }

    public enum Type {
        INDOOR, OUTDOOR
    }
}
