package com.wendersonp.holder.core.ports.driven;

public interface CryptographyServiceDrivenPort {

    byte[] hashDocumentNumber(String documentNumber);

    String maskDocumentNumber(String documentNumber);
}
