package com.harsh.journalapp.JournalNest.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.harsh.journalapp.JournalNest.dto.UserInfoDTO;
import com.harsh.journalapp.JournalNest.dto.UserLoginDTO;
import com.harsh.journalapp.JournalNest.dto.UserRegisterDTO;
import com.harsh.journalapp.JournalNest.entity.User;
import com.harsh.journalapp.JournalNest.mapper.UserMapper;
import com.harsh.journalapp.JournalNest.repository.UserRespository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private MyUserDetails myUserDetails;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private Cloudinary cloudinary;

    private static final PasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();

//    Create New User
    public String createNewUser(UserRegisterDTO userDTO) throws Exception {
        User dbUser = userRespository.findByUsername(userDTO.getUsername());

        if(dbUser != null){
            throw new Exception("username already exists");
        }

        User user1 = UserMapper.toEntity(userDTO);
        user1.setPassword(passwordEncoder.encode(user1.getPassword()));
        user1.setRoles(List.of("USER"));
        userRespository.save(user1);
        return "User created Successfully";
    }

//    Login user and generate refresh and access token
    public String login(UserLoginDTO userDTO, HttpServletResponse response) throws Exception {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(),userDTO.getPassword()));

        if(authentication.isAuthenticated()){

            String accessToken = jwtService.generateAccessToken(userDTO.getUsername());
            String refreshToken = jwtService.generateRefreshToken(userDTO.getUsername());

//
            String cookieString = "refreshToken=" + refreshToken +
                    "; Max-Age=" + (7 * 24 * 60 * 60) +
                    "; Path=/" +
                    "; HttpOnly" +
                    "; SameSite=Lax";

            response.setHeader("Set-Cookie", cookieString);


            return accessToken;

        }

        throw new Exception("Invalid username or password");
    }

//    This method is used to save user along with the journal references
    public void saveUserWithJournalRef(User user){
        userRespository.save(user);
    }

//    This method checks if refresh token is valid and generate new access token
    public String generateNewAccessToken(String token) throws Exception {
       String username =  jwtService.extractUsername(token);

       if(username != null) {
           UserDetails userDetails = myUserDetails.loadUserByUsername(username);

           if(userDetails != null){

               if(jwtService.validateToken(token,userDetails)){

                   String accessToken = jwtService.generateAccessToken(username);

                   return accessToken;

               }

           }
       }

       throw new Exception("Refresh Token is expired or invalid");
    }

//    This method updates user information
    public String updateUserInfo(UserInfoDTO userInfoDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRespository.findByUsername(username);

        if(user != null){
            user.setName(userInfoDTO.getName());
            user.setEmail(userInfoDTO.getEmail());
            user.setGender(userInfoDTO.getGender());
            user.setDateOfBirth(userInfoDTO.getDateOfBirth());
            user.setNumber(userInfoDTO.getNumber());
            userRespository.save(user);
            return "User Information Updated Successfully! ";
        }
        return null;
    }

//    Get User Information
    public UserInfoDTO getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRespository.findByUsername(username);
        if(user != null){
            return UserMapper.toDTO(user);
        }
        return null;
    }

//      Upload Profile Pic on Cloudinary
    public String uploadProfilePic(MultipartFile profilePic) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRespository.findByUsername(username);
        if(user != null) {
            Map uploadPic = cloudinary.uploader().upload(profilePic.getBytes(), ObjectUtils.emptyMap());
            user.setProfileUrl((String) uploadPic.get("url"));
            userRespository.save(user);
            return "Profile Pic Updated Successfully";
        }
        return null;
    }
}


