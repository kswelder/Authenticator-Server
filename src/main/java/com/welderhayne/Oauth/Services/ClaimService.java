package com.welderhayne.Oauth.Services;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.welderhayne.Oauth.Dtos.AuthorizationResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

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
}
