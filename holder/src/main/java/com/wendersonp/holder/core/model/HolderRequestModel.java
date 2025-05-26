package com.wendersonp.holder.core.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolderRequestModel {

    private String documentNumber;

    private String name;
}
