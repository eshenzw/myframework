package com.myframework.core.token;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by zw on 2017/7/20.
 */
public class DefaultUserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return getUserDetails(s);
    }

    private UserDetails getUserDetails(final String s) {
        JwtUser jwtUser = new JwtUser(1L, s, s, s, "", new Md5PasswordEncoder().encodePassword(s, "123"), "123", AuthorityUtils.createAuthorityList("ALLOW"), true, null);
        return jwtUser;
    }
}
