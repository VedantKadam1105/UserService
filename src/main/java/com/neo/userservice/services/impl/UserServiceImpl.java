package com.neo.userservice.services.impl;

import com.neo.userservice.entities.Hotel;
import com.neo.userservice.entities.Rating;
import com.neo.userservice.entities.User;
import com.neo.userservice.exceptions.ResourceNotFoundException;
import com.neo.userservice.external.services.HotelService;
import com.neo.userservice.external.services.RatingService;
import com.neo.userservice.repositories.UserRepository;
import com.neo.userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private HotelService hotelService;

    private RatingService ratingService;

    private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository,HotelService hotelService,RatingService ratingService) {
        this.userRepository = userRepository;
        this.hotelService=hotelService;
        this.ratingService=ratingService;
    }

    @Override
    public User saveUser(User user) {
        user.setUserId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        List<User> userList= userRepository.findAll();
        userList.forEach(user -> {

            List<Rating> ratingOfUser=ratingService.getRatingByUserID(user.getUserId());



            ratingOfUser.forEach(rating -> {

                    Hotel hotel = hotelService.getHotel(rating.getHotelId());
                    rating.setHotel(hotel);
            });
            user.setRatingList(ratingOfUser);


        });
        return userList;
    }

    @Override
    public User getUser(String userId) {
        User user= userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given ID="+userId+" is not found on the server"));


        List<Rating> ratingOfUser=ratingService.getRatingByUserID(user.getUserId());

        ratingOfUser.forEach(rating -> {

                Hotel hotel = hotelService.getHotel(rating.getHotelId());
                rating.setHotel(hotel);
        });

        user.setRatingList(ratingOfUser);
        return user;
    }

    @Override
    public User updateUser(String userId, User user) {
        User dbUser= userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given ID="+userId+" is not found on the server"));
        dbUser.setAbout(user.getAbout());
        dbUser.setName(user.getName());
        dbUser.setEmail(user.getEmail());

       return userRepository.save(dbUser);
    }

    @Override
    public String deleteUser(String userId) {
        userRepository.deleteById(userId);
        return "User got deleted";
    }
}
