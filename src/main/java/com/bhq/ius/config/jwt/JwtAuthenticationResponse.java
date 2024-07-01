package com.bhq.ius.config.jwt;

import com.bhq.ius.utils.DataUtil;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private boolean success = false;
    private JwtTokenResponse token;
    private UserDetails user;
    private List<String> role;

    public JwtAuthenticationResponse(String accessToken, UserDetails userDetails) {
        this.accessToken = accessToken;
        JwtTokenResponse tokenResponse = new JwtTokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setExpiresIn(3600000);
        this.token = tokenResponse;
        this.success = true;
        this.user = userDetails;
        if(userDetails != null) {
            if(DataUtil.notNullOrEmpty(userDetails.getAuthorities())){
                Set<String> roles = userDetails.getAuthorities().stream().map(r->r.getAuthority()).collect(Collectors.toSet());
                role = roles.stream().toList();
            }
        }
    }

    public JwtAuthenticationResponse(String accessToken,String username, String role) {
        this.accessToken = accessToken;
        JwtTokenResponse tokenResponse = new JwtTokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setExpiresIn(3600000);
        this.token = tokenResponse;
        this.success = true;
        this.role = Arrays.asList(role);

    }

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
        JwtTokenResponse tokenResponse = new JwtTokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setExpiresIn(3600000);
        this.token = tokenResponse;
        this.success = true;
    }
}
