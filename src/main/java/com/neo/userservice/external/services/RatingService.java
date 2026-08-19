package com.neo.userservice.external.services;

import com.neo.userservice.entities.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name ="RATINGSERVICE")
public interface RatingService {
    @GetMapping("/api/v1/ratings/user/{userId}")
    List<Rating> getRatingByUserID(@PathVariable("userId") String userId);
}
