package com.bhq.ius.api;

import com.bhq.ius.config.jwt.JwtAuthenticationResponse;
import com.bhq.ius.config.jwt.JwtTokenProvider;
import com.bhq.ius.constant.IusConstant;
import com.bhq.ius.constant.RoleEnum;
import com.bhq.ius.domain.dto.LoginRequest;
import com.bhq.ius.domain.dto.UserDto;
import com.bhq.ius.domain.dto.common.BaseResponseData;
import com.bhq.ius.domain.service.UserDetailCustomServiceImpl;
import com.bhq.ius.domain.service.UserService;
import com.bhq.ius.utils.DataUtil;
import com.bhq.ius.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("public/api/v1/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;


    @Value("${app.adminUser}")
    private String adminUser;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        BaseResponseData<String> response = new BaseResponseData<>();
        if (loginRequest.getUsername().isEmpty() || loginRequest.getPassword().isEmpty()) {
            response.error(HttpStatus.BAD_REQUEST, IusConstant.USERNAME_OR_PASSWORD_EMPTY);
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        try {
            UserDetails userDetails = null;
            String jwt = null;

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // TODO: Test
            // log.info(new BCryptPasswordEncoder().encode("123456a@"));
            // log.info("admin: " + new BCryptPasswordEncoder().encode("admin"));

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof UserDetails) {
                    userDetails = (UserDetails) principal;
                    Set<String> roles = userDetails.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
                    if (!DataUtil.isNullOrEmpty(roles)) {
                        jwt = jwtTokenProvider.generateToken(authentication, roles.stream().collect(Collectors.toList()).get(0).toString());
                    } else {
                        jwt = jwtTokenProvider.generateToken(authentication, RoleEnum.GUEST.name());
                    }
                    /* update login time */
                    userService.updateLoginTimeByUsername(userDetails.getUsername());

                } else {
                    log.info("===SecurityContextHolder getPrincipal: "
                            + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                }
            }

            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        } catch (BadCredentialsException ex) {
            log.error(ex.getMessage(), ex);
            response.error(HttpStatus.BAD_REQUEST, IusConstant.USERNAME_OR_PASSWORD_EMPTY);
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } catch (UsernameNotFoundException ex) {
            log.error(ex.getMessage(), ex);
            response.error(HttpStatus.BAD_REQUEST, IusConstant.USERNAME_INACTIVE);
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            response.error(HttpStatus.INTERNAL_SERVER_ERROR, IusConstant.SYSTEM_ERROR);
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @RequestMapping(value = { "/sign-out" }, method = { RequestMethod.POST })
//    @ApiOperation(value = "Revoke access token and refresh token", response = ResponseEntity.class)
//    public ResponseEntity<?> revokeAccessToken(@RequestBody(required = false) Map<String, String> aTokenMap)
//            throws BusinessException {
//        try {
//            if (aTokenMap != null) {
//                if (aTokenMap.containsKey("access_token")) {
//                }
//
//                if (aTokenMap.containsKey("refresh_token")) {
//                }
//            }
//            return ResponseEntity.ok("Success");
//        } catch (Exception ex) {
//            log.error(ex.getMessage(), ex);
//            return new ResponseEntity(new ApiResponse(false, MessageConstants.SYSTEM_ERROR), HttpStatus.BAD_REQUEST);
//        }
//    }

//    @SuppressWarnings({ "unchecked", "rawtypes" })
//    @PostMapping("/validatetoken")
//    public ResponseEntity<?> getTokenByCredentials(@Valid @RequestBody ValidateTokenRequest validateToken) {
//        String username = null;
//        String jwt = validateToken.getToken();
//        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
//            username = tokenProvider.getUsernameFromJWT(jwt);
//            // If required we can have one more check here to load the user from LDAP server
//            return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, MessageConstants.VALID_TOKEN + username));
//        } else {
//            return new ResponseEntity(new ApiResponse(false, MessageConstants.INVALID_TOKEN),
//                    HttpStatus.BAD_REQUEST);
//        }
//    }
}
