package com.example.backend.backend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.backend.Model.LoginModel;
import com.example.backend.backend.Reposistory.UserRepo;
import com.example.backend.backend.collections.User;
import com.example.backend.backend.service.UserService;
import com.example.backend.backend.utils.GenerateToken;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;


@RestController
public class UserController {
    
   @Autowired private UserService userService;
   @Autowired private UserRepo userRepo;
   @Autowired private GenerateToken generateToken;

   @PostMapping("/register/user")
   public ResponseEntity<?> registerUser(@RequestPart("image") MultipartFile file,
   @RequestPart("user") User user,
   final HttpServletRequest request){
    Optional<User>user2=userRepo.findByEmail(user.getEmail());
    if(user2.isPresent() && user2.get().isVerified()) return ResponseEntity.ok("Already registered Email");
    User user1=userService.registerUser(user,file);
    generateToken.generateToekn(user1,"Register", applicationUrl(request));
    return ResponseEntity.ok(user1.getId());
   }
   
   @GetMapping("/verifyRegistration")
   public String verfifyUserRegisration(@RequestParam ("token") String token){
     String res=userService.VerifyTokenAndValidateUser(token); 
     if(res.equalsIgnoreCase("valid")) return "user verified successfully";
     return res;
   }
   
   @PostMapping("/login/user")
   public String loginUser(@RequestBody LoginModel loginModel){
       return userService.loginUser(loginModel);
   }

   @GetMapping("/logout/user/{userId}")
   public ResponseEntity<?>logoutUser(@PathVariable ("userId") String userId){
      return userService.logoutUser(userId);
   }
   
    @GetMapping("/get/user/{userId}")
    public Optional<User> getUserByUserId(@PathVariable String userId){ 
     return userService.getUserByUserId(userId); 
     }

     //to find all users

     @GetMapping("/get/all/users")
     public List<User> getAllUser(){
       return userService.getAllUsers();
     }

     private String applicationUrl(HttpServletRequest request) {
      return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

   
   


}
