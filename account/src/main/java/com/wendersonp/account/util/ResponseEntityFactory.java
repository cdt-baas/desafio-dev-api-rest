package com.wendersonp.account.util;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseEntityFactory {

    private ResponseEntityFactory() {}

    public static <T> ResponseEntity<List<T>> createResponseEntityForPage(Page<T> page) {
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(page.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(page.getTotalPages()))
                .header("X-Page-Number", String.valueOf(page.getNumber()))
                .header("X-Page-Size", String.valueOf(page.getSize()))
                .body(page.getContent());
    }
}
