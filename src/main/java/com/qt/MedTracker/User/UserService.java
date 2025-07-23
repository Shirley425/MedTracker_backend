package com.qt.MedTracker.User;

import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class UserService {
    public List<User> getUsers() {
        return List.of(
                new User(
                        "Joe Doe",
                        30,
                        LocalDate.of(1995, Month.JANUARY, 1),
                        "joe.doe.gmail.com",
                        "PATIENT"
                )
        );
    }

}
