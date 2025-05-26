package com.wendersonp.holder.application.dto;

import com.wendersonp.holder.core.model.HolderRequestModel;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record HolderRequestDTO(
        @CPF
        @NotBlank
        String documentNumber,
        @NotBlank
        String name) {

    public HolderRequestModel toModel() {
        return new HolderRequestModel(
                    documentNumber
                            .replace(".", "")
                            .replace("-", "")
                            .trim(),
                    name
        );
    }
}
