package com.example.NextLevel.domain.member.exception;

public enum MemberException {
    NOT_FOUND("NOT_FOUND", 404),
    DUPLICATE_ID("DUPLICATED_MEMBER", 409),
    DUPLICATE_EMAIL("DUPLICATED_EMAIL", 409),
    INVALID("INVALID", 400),
    BAD_CREDENTIALS("BAD_CREDENTIALS", 401),
    NOT_MATCHED_PASSWORD("NOT_MATCHED_PASSWORD", 400),
    FORBIDDEN_ACCESS("FORBIDDEN_ACCESS", 403);

    private MemberTaskException memberTaskException;

    MemberException(final String message, final int code) {
        memberTaskException = new MemberTaskException(message, code);
    }

    public MemberTaskException get() {
        return memberTaskException;
    }
}
