package com.msa.auth.auths;

import com.msa.auth.auths.dto.LoginDto;
import com.msa.auth.auths.dto.RegisterDto;
import com.msa.auth.auths.dto.UserInfoDto;
import com.msa.auth.auths.dto.UserInfoUpdateDto;
import com.msa.auth.core.User;
import com.msa.auth.core.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "auth-service")
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    private Key key;

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${spring.application.name}")
    private String issuer;
    @Value("${jwt.expiration}")
    private Long expiration;


    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(String username, UserRoleEnum role) {
        return Jwts.builder()
                .claim("username", username)
                .claim("auth", role.getAuthority())
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean validationToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private Claims extractClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    @Transactional
    public RegisterDto register(RegisterDto registerDto) {
        if (authRepository.findByUsername(registerDto.getUsername()).orElse(null) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username 중복 불가");
        }
        if (authRepository.findByEmail(registerDto.getEmail()).orElse(null) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email 이미 존재");
        }
        User saveUser = new User(registerDto, passwordEncoder.encode(registerDto.getPassword()));
        authRepository.save(saveUser);
        return registerDto;
    }

    @Transactional(readOnly = true)
    public String login(LoginDto loginDto) {
        User user = authRepository.findByUsername(loginDto.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "username 찾을 수 없음"));
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 password");
        }
        return createToken(user.getUsername(), user.getRole());
    }

    @Transactional(readOnly = true)
    public UserInfoDto findByUsername(String username, String token) {
        User user = checkUser(username, token);
        return new UserInfoDto(user);
    }

    @Transactional
    public UserInfoDto updateByUsername(String username, String token, UserInfoUpdateDto updateDto) {
        User user = checkUser(username, token);
        return new UserInfoDto(user.updateInfo(updateDto));
    }

    @Transactional(readOnly = true)
    public List<UserInfoDto> findAll() {
        return authRepository.findAll().stream()
                .map(UserInfoDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserInfoDto deleteByUsername(String username) {
        User user = authRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND USER USERNAME: " + username));
        authRepository.delete(user);
        return new UserInfoDto(user);
    }


    private User checkUser(String username, String token) {
        if (token == null || !validationToken(token)) {throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token 정보가 잘못됨");}
        Claims claims = extractClaims(token);
        String tokenUsername = String.valueOf(claims.get("username"));
        if (!username.equals(tokenUsername)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token 의 정보와 조회할 user 의 정보가 일치하지 않음");
        return authRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user 의 정보를 찾을 수 없음"));
    }
}
