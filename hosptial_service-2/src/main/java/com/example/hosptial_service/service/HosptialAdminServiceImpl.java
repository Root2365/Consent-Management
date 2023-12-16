package com.example.hosptial_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.hosptial_service.entity.Doctor;
import com.example.hosptial_service.repo.DoctorRepo;

import javax.print.Doc;
import java.util.Map;

@Service
public class HosptialAdminServiceImpl implements HospitalAdminService {


    @Autowired
    private DoctorRepo doctorRepo;
    static String getAlphaNumericString()
    {
        int n = 32;
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz" + "!@#$%^&*()";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
//    @Override
//    public String newUser(Doctor user) {
//        DoctorRepo.save(user);
//        return "saved";
//    }
//    @Override
//    public String AddDoctor(Doctor d){
//        doctorRepo.save(d);
//        return "saved";
//    }
//
//    @Override
//    public String login(String email, String password) {
//        HospitalAdmin user = hospitalAdminRepo.findByEmailIgnoreCase(email);
//        if(user == null) return "not-found";
//        if(!user.getPassword().equals(password)){
//            return "incorrect-password";
//        }
//        return user.getAuthToken();
//    }
//    @Override
//    public String logout(String email) {
//        HospitalAdmin user = hospitalAdminRepo.findByEmailIgnoreCase(email);
//        if(user== null) return "not-found";
//        String random = getAlphaNumericString();
//        user.setAuthToken(random);
//        hospitalAdminRepo.save(user);
//        return "success";
//    }
//    @Override
//    public void deleteUser(String email) {
//        return;
//    }

    @Override
    public void updateUserDetails(String email, Map<String, String> payload) {
        Doctor user = doctorRepo.findByEmailIgnoreCase(email);
        System.out.println( email );
        if(payload.containsKey("name")){
            user.setName(payload.get("name"));
        }
//        if(payload.containsKey("email")){
//            user.setEmail(payload.get("email"));
//        }
        doctorRepo.save(user);
    }

    @Override
    public boolean changePassword(String email, Map<String, String> payload) {
        Doctor user = doctorRepo.findByEmailIgnoreCase(email);
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        System.out.println(payload.get("password"));
        boolean matches = bcrypt.matches(payload.get("password"), user.getPassword());
        if(matches!= true) {
            System.out.println("password not changed");
            return matches;
        }
        user.setPassword(bcrypt.encode(payload.get("new-password")));
        System.out.println("password changed");
        doctorRepo.save(user);
        return  matches;
    }


}
