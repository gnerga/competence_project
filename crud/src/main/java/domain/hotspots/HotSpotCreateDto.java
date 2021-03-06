package domain.hotspots;

import model.HotSpot;

public class HotSpotCreateDto {
    public final String name;
    public final String description;
    public final float longitude;
    public final float latitude;
    public final HotSpot.Type type;

    public HotSpotCreateDto(String name, String description, float longitude, float latitude, HotSpot.Type type) {
        this.name = name;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
    }
}
