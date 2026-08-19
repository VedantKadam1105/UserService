package com.neo.userservice.external.services;

import com.neo.userservice.entities.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="HOTELSERVICE")
public interface HotelService {
    @GetMapping("/api/v1/hotels/{hotelId}")
    Hotel getHotel(@PathVariable("hotelId") String hotelId);

}
