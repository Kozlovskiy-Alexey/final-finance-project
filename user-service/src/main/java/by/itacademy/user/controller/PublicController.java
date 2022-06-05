package by.itacademy.user.controller;

import by.itacademy.user.advice.ResponseError;
import by.itacademy.user.controller.dto.LoginDto;
import by.itacademy.user.controller.utils.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final UserDetailsManager userManager;
    private final PasswordEncoder encoder;

    public PublicController(UserDetailsManager userManager,
                            PasswordEncoder encoder) {
        this.userManager = userManager;
        this.encoder = encoder;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@RequestBody LoginDto loginDto) {
        try {
            UserDetails user = userManager.loadUserByUsername(loginDto.getLogin());
            return "user is already exist.";
        } catch (UsernameNotFoundException ex) {
            userManager.createUser(User.builder()
                    .username(loginDto.getLogin())
                    .password(encoder.encode(loginDto.getPassword()))
                    .roles("USER")
                    .build());
        }
        return "user registered";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody LoginDto loginDto){
        UserDetails userDetails = userManager.loadUserByUsername(loginDto.getLogin());

        if(!encoder.matches(loginDto.getPassword(), userDetails.getPassword())){
            throw new IllegalArgumentException("Password is not valid.");
        }
        return JwtTokenUtil.generateAccessToken(userDetails);
    }

}
