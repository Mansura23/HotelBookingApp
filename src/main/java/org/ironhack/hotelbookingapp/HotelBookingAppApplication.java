package org.ironhack.hotelbookingapp;

import org.ironhack.hotelbookingapp.entity.User;
import org.ironhack.hotelbookingapp.enums.Role;
import org.ironhack.hotelbookingapp.enums.Status;
import org.ironhack.hotelbookingapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class HotelBookingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelBookingAppApplication.class, args);
        System.out.println("Hotel booking app has been started");
    }
//
//    @Bean
//    CommandLineRunner init(UserRepository repo, BCryptPasswordEncoder encoder) {
//        return args -> {
//
//            System.out.println("INIT RUNNING...");
//
//            if (repo.findByEmail("admin1@gmail.com") == null) {
//
//                User admin = new User();
//                admin.setFirstName("Admin1");
//                admin.setLastName("Adminov1");
//                admin.setEmail("admin1@gmail.com");
//                admin.setNumber("+9940000000");
//                admin.setPassword(encoder.encode("admin123"));
//                admin.setRole(Role.ADMIN);
//                admin.setStatus(Status.ACTIVE);
//
//                repo.save(admin);
//
//                System.out.println("ADMIN CREATED!");
//            } else {
//                System.out.println("ADMIN ALREADY EXISTS");
//            }
//        };
//    }

}
