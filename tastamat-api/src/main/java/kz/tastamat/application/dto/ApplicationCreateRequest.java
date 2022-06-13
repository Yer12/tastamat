package kz.tastamat.application.dto;

public class ApplicationCreateRequest {

    private Sender sender;
    private Receiver receiver;
    private String index;
    private String size;
    private String email;

    public Sender getSender() {
        return sender;
    }

    public ApplicationCreateRequest setSender(Sender sender) {
        this.sender = sender;
        return this;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public ApplicationCreateRequest setReceiver(Receiver receiver) {
        this.receiver = receiver;
        return this;
    }

    public String getIndex() {
        return index;
    }

    public ApplicationCreateRequest setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getSize() {
        return size;
    }

    public ApplicationCreateRequest setSize(String size) {
        this.size = size;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ApplicationCreateRequest setEmail(String email) {
        this.email = email;
        return this;
    }
}


class Sender {
    private String name;
    private String phone;
    private Location location;

    public String getName() {
        return name;
    }

    public Sender setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Sender setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public Sender setLocation(Location location) {
        this.location = location;
        return this;
    }
}

class Receiver {
    private String name;
    private String phone;

    public String getName() {
        return name;
    }

    public Receiver setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Receiver setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}

class Location{
    private String address;
    private String city;
    private Coordinates coordinates;

    public String getAddress() {
        return address;
    }

    public Location setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Location setCity(String city) {
        this.city = city;
        return this;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Location setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }
}

class Coordinates {
    private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public Coordinates setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getLongitude() {
        return longitude;
    }

    public Coordinates setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }
}
