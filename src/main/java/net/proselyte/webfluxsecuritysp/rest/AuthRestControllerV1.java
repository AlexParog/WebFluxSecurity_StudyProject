package net.proselyte.webfluxsecuritysp.rest;

import lombok.RequiredArgsConstructor;
import net.proselyte.webfluxsecuritysp.dto.AuthRequestDto;
import net.proselyte.webfluxsecuritysp.dto.AuthResponseDto;
import net.proselyte.webfluxsecuritysp.dto.UserDto;
import net.proselyte.webfluxsecuritysp.entity.UserEntity;
import net.proselyte.webfluxsecuritysp.mapper.UserMapper;
import net.proselyte.webfluxsecuritysp.security.CustomPrincipal;
import net.proselyte.webfluxsecuritysp.security.SecurityService;
import net.proselyte.webfluxsecuritysp.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthRestControllerV1 {

    private final SecurityService securityService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto dto) {
        UserEntity entity = userMapper.mapDto2Entity(dto);
        return userService.registerUser(entity)
                .map(userMapper::mapEntity2Dto);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto requestDto) {
        return securityService.authenticate(requestDto.getUsername(), requestDto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpireAt())
                                .build()
                ));
    }

    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();

        return userService.getUserById(customPrincipal.getId())
                .map(userMapper::mapEntity2Dto);
    }
}
