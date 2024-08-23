package com.msa.auth.auths;

import com.msa.auth.auths.dto.LoginDto;
import com.msa.auth.auths.dto.RegisterDto;
import com.msa.auth.auths.dto.UserInfoDto;
import com.msa.auth.auths.dto.UserInfoUpdateDto;
import com.msa.auth.core.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {

    private final AuthService authService;
    private final String AUTH_HEADER = "Authorization";
    private final String ROLE_HEADER = "X-ROLE";

    @PostMapping("/register")
    public ResponseEntity<RegisterDto> register(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(authService.register(registerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> signin(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserInfoDto> findByUsername(@PathVariable String username, @RequestHeader(AUTH_HEADER) String token) {
        return ResponseEntity.ok(authService.findByUsername(username, token));
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserInfoDto> updateByUsername(@PathVariable String username, @RequestBody UserInfoUpdateDto updateDto, @RequestHeader(AUTH_HEADER) String token) {
        return ResponseEntity.ok(authService.updateByUsername(username, token, updateDto));
    }

    @GetMapping()
    public ResponseEntity<List<UserInfoDto>> findAll(@RequestHeader(ROLE_HEADER) String role) {
        if (!ROLE_ADMIN_CHECK(role)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "YOUR ARE NOT ADMIN");
        return ResponseEntity.ok(authService.findAll());
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<UserInfoDto> deleteByUsername(@PathVariable String username, @RequestHeader(ROLE_HEADER) String role) {
        if (!ROLE_ADMIN_CHECK(role)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "YOUR ARE NOT ADMIN");
        return ResponseEntity.ok(authService.deleteByUsername(username));
    }

    private boolean ROLE_ADMIN_CHECK(String role) {
        if (role == null || role.equals(UserRoleEnum.USER.getAuthority())) return false;
        return role.equals(UserRoleEnum.ADMIN.getAuthority());
    }
}
