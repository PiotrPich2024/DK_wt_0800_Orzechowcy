package pl.edu.agh.to.przychodnia.Room;

import jakarta.persistence.*;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;

import java.util.List;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    // czytamy to w taki sposób: 1 cyfra liczby to piętro
    // następne cyfry tworzą nr gabinetu
    @Column(nullable = false)
    private int roomNumber;

    // Do wyświetlania szczegółów
    @Column()
    private String roomDescription;

    @OneToMany
    private List<Schedule> schedule;

    public Room(int roomNumber, String roomDescription) {
        this.roomNumber = roomNumber;
        this.roomDescription = roomDescription;
    }

    public Room() {

    }

    @Override
    public String toString() {
        String s = "ID gabinetu: " + id + ", Numer gabinetu: " + roomNumber + ", Opis: " +  roomDescription;
        return s;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getId() {
        return id;
    }

    public List<Schedule> getSchedule(){
        return schedule;
    }
}
