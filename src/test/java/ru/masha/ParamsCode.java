package ru.masha;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParamsCode {
    TITLE_CONFLICT ("titleConflict"),
    CODE_CONFLICT ("codeConflict");

    private final String paramCode;
}
