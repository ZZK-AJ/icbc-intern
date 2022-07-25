package com.icbcintern.prepaycard;

import com.icbcintern.prepaycard.contract.InstanceUtils;
import org.junit.jupiter.api.Test;
import org.wasmer.Instance;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-07-25 10:29
 **/
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
}
