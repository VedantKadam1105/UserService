package com.neo.userservice.entities;

import lombok.Data;

@Data
public class Hotel {
    private String hotelId;
    private String name;
    private String location;
    private String about;
}
