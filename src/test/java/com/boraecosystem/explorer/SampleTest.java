package com.boraecosystem.explorer;

import com.boraecosystem.explorer.crawler.utils.InputParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class SampleTest {

    @Test
    public void test() {
        String methodId1 = Hash.sha3String("transfer(address,uint256)");
        String methodId2 = Hash.sha3String("Transfer(address,uint256)");
        String methodId3 = Hash.sha3String("transfer(address, uint256)");
        System.out.println(methodId1);
        System.out.println(methodId2);
        System.out.println(methodId3);
    }

    @Test
    public void to() {
        String input = "0x40c10f190000000000000000000000003ecfbe1ea2c367d32c4cf5db4d12d63c1fe1ac100000000000000000000000000000000000000000000000000000000000001194";

        List<String> strings = InputParser.chunk64(input.substring(10));

        for (String str : strings) {
            System.out.println(str);
        }

    }

    @Test
    public void aaa() throws UnsupportedEncodingException {
        byte[] bt = new byte[]{97, 97, 97, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Bytes32 data = new Bytes32(bt);
        System.out.println(data.getTypeAsString());
        System.out.println(data.getValue());
        System.out.println(new String(data.getValue(), "UTF-8"));
        System.out.println(Numeric.toHexStringNoPrefix(data.getValue()));


    }

    @Test
    public void list() {
        List<String> strings = new ArrayList<>();
        strings.add("a");
        strings.add("b");

        System.out.println(strings.get(2));

    }
}
