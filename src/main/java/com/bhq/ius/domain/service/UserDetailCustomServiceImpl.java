package com.bhq.ius.domain.service;

import com.bhq.ius.constant.RoleEnum;
import com.bhq.ius.domain.repository.UserRepository;
import com.bhq.ius.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UserDetailCustomServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Value("${app.adminUser}")
    private String adminUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.bhq.ius.domain.entity.User user = userRepository.findByUsername(username);
        if(DataUtil.isNullOrEmpty(user)) {
            throw new UsernameNotFoundException("User not found");
        }
        SimpleGrantedAuthority simpleGrantedAuthority;
        if(adminUser.trim().equals(user.getUsername().trim())) {
            simpleGrantedAuthority = new SimpleGrantedAuthority(RoleEnum.ADMIN.name());
        } else {
            simpleGrantedAuthority = new SimpleGrantedAuthority(RoleEnum.USER.name());
        }
        User userDetails = new User(
                user.getUsername()
                , user.getPassword()
                , Collections.singleton(simpleGrantedAuthority));
        return userDetails;
    }


}
