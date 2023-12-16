package com.example.hosptial_service.config;
import com.example.hosptial_service.repo.DoctorRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  private final DoctorRepo doctorRepo;

  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    String requestToken = request.getHeader("Authorization");
    Enumeration<String> headerNames = request.getHeaderNames();

    while(headerNames.hasMoreElements())
    {
      System.out.println(headerNames.nextElement());
    }
    // Bearer 2352523sdgsg

    System.out.println(requestToken);

    String username = null;

    String token = null;

    if (requestToken != null && requestToken.startsWith("Bearer")) {

      token = requestToken.substring(7);

      try {
        username = this.jwtService.extractUsername(token);
      } catch (IllegalArgumentException e) {
        System.out.println("Unable to get Jwt token");
      } catch (ExpiredJwtException e) {
        System.out.println("Jwt token has expired");
      } catch (MalformedJwtException e) {
        System.out.println("invalid jwt");

      }

    } else {
      System.out.println("Jwt token does not begin with Bearer");
    }

    // once we get the token , now validate
//    String id = request.getParameter("user_id");
//    Integer pk = Integer.parseInt(id);
//    var doctor  = doctorRepo.findById(user_id);
//
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

      if (this.jwtService.isTokenValid(token, userDetails)) {
        // shi chal rha hai
        // authentication karna hai

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

      } else {
        System.out.println("Invalid jwt token");
      }

    } else {
      System.out.println("username is null or context is not null");
    }


    filterChain.doFilter(request, response);
  }
}
