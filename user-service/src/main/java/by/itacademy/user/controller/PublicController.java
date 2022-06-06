package by.itacademy.user.controller;

import by.itacademy.user.controller.dto.LoginDto;
import by.itacademy.user.controller.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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

            log.info("NEW User registered. " + loginDto);
        }
        return "user registered";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody LoginDto loginDto){
        UserDetails userDetails = userManager.loadUserByUsername(loginDto.getLogin());

        if(!encoder.matches(loginDto.getPassword(), userDetails.getPassword())){
            log.error("Login attempt with wrong password " + loginDto.getPassword());
            throw new IllegalArgumentException("Password is not valid.");
        }
        return JwtTokenUtil.generateAccessToken(userDetails);
    }

}
