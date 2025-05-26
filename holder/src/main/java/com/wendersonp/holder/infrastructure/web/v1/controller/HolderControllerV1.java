package com.wendersonp.holder.infrastructure.web.v1.controller;

import com.wendersonp.holder.application.dto.HolderRequestDTO;
import com.wendersonp.holder.application.dto.HolderResponseDTO;
import com.wendersonp.holder.application.service.HolderApplicationService;
import com.wendersonp.holder.infrastructure.web.v1.controller.docs.HolderControllerV1ApiDocs;
import com.wendersonp.holder.infrastructure.web.v1.routes.V1Routes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(V1Routes.HOLDER_PATH)
@RequiredArgsConstructor
public class HolderControllerV1 implements HolderControllerV1ApiDocs {

    private final HolderApplicationService holderApplicationService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public HolderResponseDTO save(@Valid @RequestBody HolderRequestDTO holderRequestDTO) {
        log.info("Saving holder {}", holderRequestDTO.name());
        return holderApplicationService.save(holderRequestDTO);
    }

    @GetMapping("/document")
    @ResponseStatus(value = HttpStatus.OK)
    public HolderResponseDTO findByDocument(@RequestParam String documentNumber) {
        log.info("Finding holder by document");
        return holderApplicationService.findByDocument(documentNumber);
    }

    @GetMapping("/{identifier}")
    @ResponseStatus(value = HttpStatus.OK)
    public HolderResponseDTO findByIdentifier(@PathVariable UUID identifier) {
        log.info("Finding holder by identifier");
        return holderApplicationService.findByIdentifier(identifier);
    }

    @DeleteMapping("/{identifier}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteByIdentifier(@PathVariable UUID identifier) {
        log.info("Deleting holder by identifier {}", identifier);
        holderApplicationService.deleteByIdentifier(identifier);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<HolderResponseDTO> findAll() {
        log.info("Finding all holder");
        return holderApplicationService.findAll();
    }

    @PatchMapping("/{identifier}/reactivate")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void reactivate(@PathVariable UUID identifier) {
        log.info("Reactivate holder by identifier {}", identifier);
        holderApplicationService.reactivate(identifier);
    }

}
