package com.wendersonp.account.infrastructure.clients;

import com.wendersonp.account.infrastructure.clients.dto.HolderResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "holder-service")
public interface HolderFeignClient {

    @GetMapping(value = "/api/v1/holder/document", produces = "application/json")
    HolderResponseDTO findByDocumentNumber(@RequestParam String documentNumber);

    @GetMapping(value = "/api/v1/holder/{identifier}", produces = "application/json")
    HolderResponseDTO findByIdentifier(@PathVariable UUID identifier);

    @DeleteMapping(value = "/api/v1/holder/{identifier}", produces = "application/json")
    void removeByIdentifier(@PathVariable UUID identifier);

    @PatchMapping(value = "/api/v1/holder/{identifier}/reactivate", produces = "application/json")
    void reactivate(@PathVariable UUID identifier);
}
