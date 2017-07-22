package com.myframework.core.token;

import com.myframework.util.SpringContextUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zw on 2017/7/19.
 */
public class TokenManager
{

	public static final String CONSTANT_AUTHENTICATION = "authenticationManager";

	public static JwtTokenUtil jwtTokenUtil = SpringContextUtil.getBean(JwtTokenUtil.class);

	public static boolean isTokenEnable(){
	    return jwtTokenUtil.isTokenEnable();
    }

	public static String getTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader(jwtTokenUtil.getTokenHeader());
        return  token;
    }

	public static boolean isTokenExpired(String token)
	{
		return jwtTokenUtil.isTokenExpired(token);
	}

	public static boolean validateToken(String token, UserDetails userDetails)
	{
		return jwtTokenUtil.validateToken(token, userDetails);
	}

	public static ResponseEntity<?> createAuthenticationToken(JwtAuthenticationRequest authenticationRequest,
			Device device, UserDetails userDetails) throws AuthenticationException
	{

		AuthenticationManager authenticationManager = (AuthenticationManager) SpringContextUtil
				.getBean(CONSTANT_AUTHENTICATION);

		// Perform the security
		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
						authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Reload password post-security so we can generate token
		JwtTokenUtil jwtTokenUtil = SpringContextUtil.getBean(JwtTokenUtil.class);
		final String token = jwtTokenUtil.generateToken(userDetails, device);

		// Return the token
		return new ResponseEntity<JwtAuthenticationResponse>(new JwtAuthenticationResponse(token), HttpStatus.OK);
	}

	public static ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request, UserDetails userDetails)
	{
		String token = request.getHeader(jwtTokenUtil.getTokenHeader());
		String username = jwtTokenUtil.getUsernameFromToken(token);
		JwtUser user = (JwtUser) userDetails;

		if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate()))
		{
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return new ResponseEntity<JwtAuthenticationResponse>(new JwtAuthenticationResponse(refreshedToken),
					HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	public List<String> getAuthentications()
	{
		Collection<? extends GrantedAuthority> auths = SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities();
		List<String> list = new ArrayList<String>();
		for (GrantedAuthority auth : auths)
		{
			list.add(auth.getAuthority());
		}
		return list;
	}
}
