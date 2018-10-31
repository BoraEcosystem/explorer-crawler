package com.boraecosystem.explorer.crawler.constant;

import java.util.Arrays;

public enum Method {
    TRANSFER("5c09329c"),
    TRANSFER_FROM("899fe659"),
    APPROVE("426a8493"),
    INCREASE_APPROVAL("a95e5b8b"),
    DECREASE_APPROVAL("70552cd2"),
    MINT("156e29f6"),
    BURN("f5298aca"),
    UNDEFINED("");

    private final String methodHash;

    Method(String methodHash) {
        this.methodHash = methodHash;
    }

    public static Method fromHash(String input) {
        if (input == null || input.length() < 10) return null;

        String methodSignature = input.substring(2, 10);
        return Arrays.stream(Method.values())
            .filter(method -> method.methodHash.equals(methodSignature))
            .findFirst()
            .orElse(UNDEFINED);
    }

}
