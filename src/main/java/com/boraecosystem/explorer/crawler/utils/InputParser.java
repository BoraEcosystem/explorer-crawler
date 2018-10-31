package com.boraecosystem.explorer.crawler.utils;

import com.google.common.base.Splitter;
import org.web3j.abi.TypeDecoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.utils.Strings;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class InputParser {

    private static final String HEX_PREFIX = "0x";

    public static <T extends Type> T decode(String value, Class<T> type) {
        try {
            final Method decode = TypeDecoder.class.getDeclaredMethod("decode", String.class, int.class, Class.class);
            decode.setAccessible(true);
            return (T) decode.invoke(null, value, 0, type);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static List<String> chunk64(String value) {
        if (Strings.isEmpty(value) || HEX_PREFIX.equals(value))
            return Collections.emptyList();
        if (value.startsWith(HEX_PREFIX))
            value = value.substring(2);

        return Splitter.fixedLength(64).splitToList(value);
    }
}
