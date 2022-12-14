package com.example.backend.backend.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import com.example.backend.backend.Model.LoginModel;
import com.example.backend.backend.Reposistory.TokenRepo;
import com.example.backend.backend.Reposistory.UserRepo;
import com.example.backend.backend.collections.Token;
import com.example.backend.backend.collections.User;
import com.example.backend.backend.config.SecurityConfigurer;
import com.example.backend.backend.utils.DataBucketUtil;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SecurityConfigurer securityConfigurer;
    @Autowired 
    private TokenRepo tokenRepo;
    @Autowired DataBucketUtil dataBucketUtil;

    // register a user 
    @Override
    public User registerUser(User user,MultipartFile file) {
        user.setPassword(securityConfigurer.passwordEncoder().encode(user.getPassword()));
        String originalFileName=file.getOriginalFilename();
        if(originalFileName==null){
           return null;
         }
        Path path=new File(originalFileName).toPath();
        String url=null;
         try {
          String contentType=Files.probeContentType(path);
           try {
            url=dataBucketUtil.uploadFile(file,originalFileName,contentType);
          } catch (Exception e) {
            e.printStackTrace();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
        user.setImageURL(url);
       return userRepo.save(user);
    }

    
   // to find a user based on user id
   @Override
   public Optional<User> getUserByUserId(String user_id) {
       return userRepo.findById(user_id);
   }

   // to verify token
   @Override
   public String VerifyTokenAndValidateUser(String token) {
     Token tkn=tokenRepo.findByToken(token);
     if(tkn==null) return "Invalid";
     Optional<User>user=userRepo.findById(tkn.getUserId());
     if(user==null) return "Invalid";
     Calendar cal=Calendar.getInstance();
       if(tkn.getExpirationTime().getTime()-cal.getTime().getTime()<=0){
           tokenRepo.delete(tkn);
           return "Token expired";
       }
       User u=user.get();
       u.setVerified(true);
       userRepo.save(u);
       tokenRepo.delete(tkn);
       return "valid";
   }

   // to login user
   @Override
   public String loginUser(LoginModel loginModel) {
        if(loginModel==null) return "No credential found";
        Optional<User> user=userRepo.findByEmail(loginModel.getEmail());
        if(user.isEmpty()) return "user not found associated to this email";
        if(securityConfigurer.passwordEncoder().matches(loginModel.getPassword(), user.get().getPassword())){
           user.get().setHasLoggedIn(true);
           userRepo.save(user.get());
           return user.get().getId();
        }
        return "Bad Credential";
   }

   // logout the user
   @Override
   public ResponseEntity<?> logoutUser(String userId) {
       Optional<User>user=userRepo.findById(userId);
       if(user.isEmpty()) return ResponseEntity.ok("User not found");
       user.get().setHasLoggedIn(false);
       userRepo.save(user.get());
       return ResponseEntity.ok("logout successfullly");
   }

  // to find all users
  @Override
  public List<User> getAllUsers() {
     return userRepo.findAll();
  }

    
    
}
