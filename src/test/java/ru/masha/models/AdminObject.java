package ru.masha.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class AdminObject {
    UUID id;
    String code;
    String title;
    String objectTypeCode;
    String systemCode;

    public AdminObject(String code, String title, String objectTypeCode, String systemCode) {
        this.code = code;
        this.title = title;
        this.objectTypeCode = objectTypeCode;
        this.systemCode = systemCode;
    }
}
