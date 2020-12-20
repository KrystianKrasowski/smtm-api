package com.smtm.application.security.token.v1;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smtm.application.SmtmMediaTypes;
import com.smtm.application.security.users.v1.CredentialsDto;
import com.smtm.security.api.Authentication;

@RestController
@RequestMapping(path = "/security/token")
public class TokenController {

    private final Authentication authentication;

    public TokenController(Authentication authentication) {
        this.authentication = authentication;
    }

    @PostMapping(
        consumes = SmtmMediaTypes.Security.V1.CREDENTIALS_VALUE,
        produces = SmtmMediaTypes.Security.V1.TOKEN_VALUE
    )
    public ResponseEntity<?> createToken(@RequestBody CredentialsDto credentialsDto) {
        return Optional.ofNullable(authentication.authenticate(credentialsDto.getEmail(), credentialsDto.getPassword()))
            .map(token -> ResponseEntity.ok(TokenDto.of(token)))
            .orElseGet(() -> ResponseEntity.status(401).build());
    }
}
