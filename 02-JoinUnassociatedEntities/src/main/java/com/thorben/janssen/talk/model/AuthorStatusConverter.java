package com.thorben.janssen.talk.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AuthorStatusConverter implements AttributeConverter<AuthorStatus, String> {
    @Override
    public String convertToDatabaseColumn(AuthorStatus attribute) {
        return switch (attribute) {
            case PUBLISHED -> "P";
            case SELF_PUBLISHED -> "S";
            case NOT_PUBLISHED -> "N";
        };
    }

    @Override
    public AuthorStatus convertToEntityAttribute(String dbData) {
        return switch(dbData) {
            case "P" -> AuthorStatus.PUBLISHED;
            case "S" -> AuthorStatus.SELF_PUBLISHED;
            case "N" -> AuthorStatus.NOT_PUBLISHED;
            default -> throw new IllegalStateException("AuthorStatus [" + dbData +"] not supported");
        };
    }
}
