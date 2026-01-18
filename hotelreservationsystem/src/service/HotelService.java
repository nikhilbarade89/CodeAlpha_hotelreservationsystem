package service;

import model.Room;
import model.Reservation;

import java.io.*;
import java.util.*;

public class HotelService {

    private List<Room> rooms = new ArrayList<>();
    private File file = new File("src/data/reservations.txt");

    public HotelService() {
        rooms.add(new Room(101, "Standard", 1500));
        rooms.add(new Room(102, "Standard", 1500));
        rooms.add(new Room(103, "Standard", 1600));
        rooms.add(new Room(104,"Deluxe",3000));
        rooms.add(new Room(105,"Deluxe",3200));
        rooms.add(new Room(106,"Deluxe",3400));
        rooms.add(new Room(107,"Suite",4000));
        rooms.add(new Room(108,"Suite",4500));
        rooms.add(new Room(109,"Suite",4300));
        loadReservedrooms();

    }

    public List<Room> getRooms() {
        return rooms;
    }

    public String reserveRoom(String username, int roomNumber) throws IOException {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber && room.isAvailable()) {
                room.setAvailable(false);
                String id = UUID.randomUUID().toString().substring(0, 8);
                Reservation r = new Reservation(id, username, roomNumber, "RESERVED");
                saveReservation(r);
                return id;
            }
        }
        return null;
    }

    public boolean cancelReservation(String reservationId, String username) throws IOException {
        List<String> updated = new ArrayList<>();
        boolean found = false;
        int cancelledRoomNo=-1;

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data[0].equals(reservationId) && data[1].equals(username) && data[3].equals("RESERVED")) {
                updated.add(data[0] + "," + data[1] + "," + data[2] + ",CANCELLED");
                cancelledRoomNo = Integer.parseInt(data[2]);
                found = true;
            } else {
                updated.add(line);
            }
        }
        br.close();

        FileWriter fw = new FileWriter(file);
        for (String s : updated) fw.write(s + "\n");
        fw.close();
        if(found && cancelledRoomNo !=-1){
            for(Room r: rooms){
                if(r.getRoomNumber()==cancelledRoomNo){
                    r.setAvailable(true);
                    break;
                }
            }
        }

        return found;
    }

    private void saveReservation(Reservation r) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        fw.write(r.toFileString() + "\n");
        fw.close();
    }

    public List<String> getAllReservations() throws IOException {
        List<String> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) list.add(line);
        br.close();
        return list;
    }
    private void loadReservedrooms(){
        if(!file.exists()) return;
        Map<Integer,String> roomStatusMap= new HashMap<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while((line = br.readLine())!= null){
                String[] data= line.split(",");
                int roomNo=Integer.parseInt(data[2]);
                String status = data[3];
                roomStatusMap.put(roomNo, status);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        for(Room r: rooms){
            String status= roomStatusMap.get(r.getRoomNumber());
            if("RESERVED".equals(status)){
                r.setAvailable(false);
            }else {
                r.setAvailable(true);
            }
        }
    }
}