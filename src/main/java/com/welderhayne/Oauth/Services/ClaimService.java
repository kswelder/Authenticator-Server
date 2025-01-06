package com.welderhayne.Oauth.Services;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.welderhayne.Oauth.Dtos.AuthorizationResponse;
import com.welderhayne.Oauth.Exceptions.XssCharTokenInvalidException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimService {
    @SneakyThrows
    public AuthorizationResponse getClaims(String authorization) {
        String token = authorization.replace("Bearer ", "").trim();

        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

        return new AuthorizationResponse(
                claimsSet.getSubject(),
                claimsSet.getExpirationTime()
        );
    }

    public void sanatizationString(String input) {
        char[] tokensInvalids = "<>;:`´&|^".toCharArray();
        List<Character> findedInvalidTokens = List.of();

        StringBuilder sanitized = new StringBuilder();

        for (char c: input.toCharArray()) {
            boolean isValid = true;

            for (char invalid : tokensInvalids) {
                if (c == invalid) {
                    isValid = false;
                    findedInvalidTokens.add(c);
                }
            }
            if (isValid) {
                sanitized.append(c);
            }
        }

        if (findedInvalidTokens.size() > 0) {
            throw new XssCharTokenInvalidException("Invalid tokens detected: " + findedInvalidTokens.stream().toString());
        }
    }
}
