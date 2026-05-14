package org.vsu.catalogservice.clients;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.vsu.catalogservice.dto.AssignRoleDto;

@FeignClient(name = "auth-service", path = "/api/v1/auth")
public interface AuthServiceClient {
    @PostMapping("/roles/assign")
    ResponseEntity<String> assignRoleToUser(@RequestBody @Valid AssignRoleDto roleDto);
}
