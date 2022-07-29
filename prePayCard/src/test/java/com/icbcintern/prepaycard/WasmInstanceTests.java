package com.icbcintern.prepaycard;

import com.icbcintern.prepaycard.contract.InstanceUtils;
import com.icbcintern.prepaycard.contract.StringUtils;
import com.icbcintern.prepaycard.mapper.WalletMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wasmer.Instance;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-07-25 10:29
 **/
@SpringBootTest
public class WasmInstanceTests {

    @Test
    void wasmInstance() throws Exception {
        String wasmPaths = Test.class.getClassLoader().getResource("lib.wasm").toURI().toString();
        Instance instance = InstanceUtils.getWasmInstance(wasmPaths);
        {
            long start = System.currentTimeMillis();
            Object[] results = instance.exports.
                    getFunction("sum").apply(1, 2);
            long end = System.currentTimeMillis();
            System.out.println("结果:" + results[0] + " 耗时:" + (end - start) + "ms");
        }
    }

    @Test
    void refund() throws Exception {
        String wasmPaths = Test.class.getClassLoader().getResource("lib.wasm").toURI().toString();
        Instance instance = InstanceUtils.getWasmInstance(wasmPaths);
        {
            long start = System.currentTimeMillis();
            Object[] results = instance.exports.
                    getFunction("refund").apply(1L, 2L);
            long end = System.currentTimeMillis();
            System.out.println("结果:" + results[0] + " 耗时:" + (end - start) + "ms");
        }
    }

    @Test
    void transfer() throws Exception {
        String wasmPaths = Test.class.getClassLoader().getResource("lib.wasm").toURI().toString();
        Instance instance = InstanceUtils.getWasmInstance(wasmPaths);
        {
            long start = System.currentTimeMillis();
            Object[] results = instance.exports.
                    getFunction("transfer").apply(1L, 2L,200L);
            long end = System.currentTimeMillis();
            System.out.println("结果:" + results[0] + " 耗时:" + (end - start) + "ms");
        }
    }

    @Autowired
    WalletMapper walletMapper;
    @Test
    void String() throws Exception {
        String wasmPaths = Test.class.getClassLoader().getResource("lib.wasm").toURI().toString();
        Instance instance = InstanceUtils.getWasmInstance(wasmPaths);
        StringUtils stringUtils = new StringUtils(instance);
//        ImportFunction.walletMapper = walletMapper;
        {
            long start = System.currentTimeMillis();
            String wallet_id = "a4dad2dc-0a71-4ef3-89f8-3bae3bcef4e9";
//            byte[] walletBytes = wallet_id.getBytes(StandardCharsets.UTF_8);
//            Integer walletPtr = (Integer) instance.exports.getFunction("allocate").apply(walletBytes.length+1)[0];
//            Memory memory = instance.exports.getMemory("memory");
//            ByteBuffer mbf = memory.buffer();
//            mbf.position(walletPtr);
//            mbf.put(walletBytes);
            int walletPtr = stringUtils.addString(wallet_id);
            Object[] results = instance.exports.
                    getFunction("get_wallet_by_id").apply(walletPtr);

            Integer resultPtr = (Integer) results[0];

            String res = stringUtils.getString(resultPtr);
            long end = System.currentTimeMillis();
            System.out.println("结果:" + res + " 耗时:" + (end - start) + "ms");
        }
    }
}
