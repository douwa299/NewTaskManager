    package com.TaskManager.TaskManager.controll.auth;


    import com.TaskManager.TaskManager.dto.AuthentificationRequest;
    import com.TaskManager.TaskManager.dto.AuthentificationResponse;
    import com.TaskManager.TaskManager.dto.SignUpRequest;
    import com.TaskManager.TaskManager.dto.UserDto;
    import com.TaskManager.TaskManager.entities.User;
    import com.TaskManager.TaskManager.repositories.UserRepository;
    import com.TaskManager.TaskManager.services.auth.AuthService;
    import com.TaskManager.TaskManager.services.jwt.UserService;
    import com.TaskManager.TaskManager.utils.JwtUtils;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.HttpStatusCode;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.security.authentication.AuthenticationManager;

    import java.util.Optional;

    @CrossOrigin("*")
    @RestController
    @RequestMapping("/api/auth")
    @RequiredArgsConstructor
    public class AuthController {


        private final AuthService authService;
        private final UserRepository userRepository;
        private final JwtUtils jwtUtils;
        private final UserService userService;
        private final AuthenticationManager authenticationManager;

        @PostMapping("/signup")
        public ResponseEntity<?> signupUser (@RequestBody SignUpRequest signUpRequest){
            if (authService.hasUserWithEmail(signUpRequest.getEmail()))
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exists with this email");
            UserDto  createdUserDto=authService.signupUser(signUpRequest);
            if( createdUserDto == null )
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
        }

        @PostMapping("/login")
        public AuthentificationResponse login (@RequestBody AuthentificationRequest authentificationRequest){
                        try{
                            authenticationManager.authenticate(
                                     new UsernamePasswordAuthenticationToken(
                                             authentificationRequest.getEmail(),
                                             authentificationRequest.getPassword()));
                        }catch(BadCredentialsException e){
                            throw new BadCredentialsException("Incorrect username or password");
                        }
                        final UserDetails userDetails=userService.userDetailService().loadUserByUsername(authentificationRequest.getEmail());
                        //Avoid NullPointerException    safer to avoid null will return an empty response in case no user was found
                        Optional<User> optionalUser=userRepository.findFirstByEmail(authentificationRequest.getEmail());
                        final String jwtToken = jwtUtils.generateToken(userDetails);
                        AuthentificationResponse authentificationResponse=new AuthentificationResponse();

                        if(optionalUser.isPresent()){
                            authentificationResponse.setUserId(optionalUser.get().getId());
                            authentificationResponse.setJwt(jwtToken);
                            authentificationResponse.setUserRole(optionalUser.get().getUserRole());

                        }
                        return  authentificationResponse;




        }
    }
