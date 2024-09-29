package com.example.reservationsystem.vehicle.domain;

import com.example.reservationsystem.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BUS")
@NoArgsConstructor
public class Bus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long busId;

    private String busName;
    private String busNumber;
    private int capacity;

    private Bus(String busName, String busNumber, int capacity) {
        this.busName = busName;
        this.busNumber = busNumber;
        this.capacity = capacity;
    }

    public static Bus create(String busName, String busNumber, int capacity) {
        return new Bus(busName, busNumber, capacity);
    }

    public int getBusCapacity() {
        return this.capacity;
    }

}
