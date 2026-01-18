package model;

public class Reservation {
    private String reservationId;
    private String username;
    private int roomNumber;
    private String status;

    public Reservation(String reservationId, String username, int roomNumber, String status) {
        this.reservationId = reservationId;
        this.username = username;
        this.roomNumber = roomNumber;
        this.status = status;
    }

    public String toFileString() {
        return reservationId + "," + username + "," + roomNumber + "," + status;
    }
}