package com.harsh.journalapp.JournalNest.services;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60); //7 days

            response.addCookie(cookie);

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
}
