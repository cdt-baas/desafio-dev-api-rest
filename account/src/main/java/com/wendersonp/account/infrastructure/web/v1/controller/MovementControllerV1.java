package com.wendersonp.account.infrastructure.web.v1.controller;

import com.wendersonp.account.application.dto.MovementResponseDTO;
import com.wendersonp.account.application.service.MovementApplicationService;
import com.wendersonp.account.infrastructure.web.v1.controller.docs.MovementControllerV1ApiDocs;
import com.wendersonp.account.infrastructure.web.v1.routes.V1Routes;
import com.wendersonp.account.util.ResponseEntityFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(V1Routes.MOVEMENT_PATH)
public class MovementControllerV1 implements MovementControllerV1ApiDocs {

    private final MovementApplicationService movementApplicationService;

    @GetMapping
    public ResponseEntity<List<MovementResponseDTO>> getMovementsWithinAPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beginningDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endingDate,
            @RequestParam UUID accountIdentifier,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<MovementResponseDTO> movementPage = movementApplicationService.getMovementsWithinAPeriod(beginningDate, endingDate, accountIdentifier, pageable);
        return ResponseEntityFactory.createResponseEntityForPage(movementPage);
    }
}
