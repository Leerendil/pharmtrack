package org.vsu.authservice.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.vsu.authservice.clients.UserServiceClient;
import org.vsu.authservice.dto.RegisterDto;
import org.vsu.authservice.dto.LoginDto;
import org.vsu.authservice.dto.UserDto;
import org.vsu.authservice.dto.UserResponse;
import org.vsu.authservice.utils.exceptions.KeycloakLoginException;
import org.vsu.authservice.utils.exceptions.KeycloakRegisterException;
import org.vsu.authservice.utils.exceptions.UserAlreadyExistsException;
import org.vsu.authservice.utils.exceptions.UserServiceClientException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final UserServiceClient userServiceClient;
    private final Keycloak keycloakAdminClient;

    public UserResponse register(RegisterDto registerDto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(registerDto.getUsername());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setEmailVerified(true);
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(registerDto.getRawPassword());
        credential.setTemporary(false);

        user.setCredentials(List.of(credential));

        try (Response response = keycloakAdminClient.realm(realm).users().create(user)) {
            if (response.getStatus() == HttpStatus.SC_CREATED) {
                String path = response.getLocation().getPath();
                UUID keycloakId = UUID.fromString(path.substring(path.lastIndexOf("/") + 1));

                UserDto userDto = UserDto.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .keycloakId(keycloakId)
                        .build();
                try {
                    return userServiceClient.create(userDto).getBody();
                } catch (Exception e) {
                    keycloakAdminClient
                            .realm(realm)
                            .users()
                            .get(keycloakId.toString())
                            .remove();
                    throw new UserServiceClientException();
                }
            } else if (response.getStatus() == HttpStatus.SC_CONFLICT) {
                throw new UserAlreadyExistsException();
            } else {
                throw new KeycloakRegisterException(response);
            }
        }
    }

    public AccessTokenResponse login(LoginDto loginDto) {
        try (Keycloak userClient = KeycloakBuilder.builder()
                .realm(realm)
                .grantType("password")
                .clientId(clientId)
                .serverUrl(serverUrl)
                .clientSecret(clientSecret)
                .username(loginDto.getUsername())
                .password(loginDto.getRawPassword())
                .build()
        ) {
            return userClient.tokenManager().getAccessToken();
        } catch (Exception e) {
            throw new KeycloakLoginException();
        }
    }

    public String assignRoleToUser(String keycloakId, String roleName) {
        var userResource = keycloakAdminClient.realm(realm).users().get(keycloakId);

        String clientUuid = keycloakAdminClient.realm(realm).clients()
                .findByClientId(clientId).get(0).getId();

        var role = keycloakAdminClient.realm(realm).clients()
                .get(clientUuid).roles().get(roleName).toRepresentation();

        userResource.roles().clientLevel(clientUuid).add(List.of(role));

        return "Role successfully assigned!";
    }
}
