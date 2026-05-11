package org.vsu.orderservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.vsu.orderservice.dto.MedicineBasicInfo;

@FeignClient(name = "catalog-service", path = "/api/v1/medicines")
public interface CatalogClientService {
    @GetMapping("/id/{id}")
    ResponseEntity<MedicineBasicInfo> getById(@PathVariable("id") Long id);
}
