package com.icbcintern.prepaycard.contract;

import com.icbcintern.prepaycard.contract.function.ImFunc;
import com.icbcintern.prepaycard.contract.function.ImQueryBalanceByWalletId;
import com.icbcintern.prepaycard.contract.function.ImportFunction;
import org.wasmer.Imports;
import org.wasmer.Instance;
import org.wasmer.Module;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * description:wasm加载与函数导入
 * @author: He Yihui
 * @create: 2022-07-25 10:19
 **/
public class InstanceUtils {
    public static Instance getWasmInstance(String wasmPaths) throws Exception {
        System.out.println(wasmPaths);

        System.out.println(ImQueryBalanceByWalletId.walletMapper);
        Path wasmPath = Paths.get(new URI(wasmPaths));

        byte[] wasmBytes = Files.readAllBytes(wasmPath);

        Module module = new Module(wasmBytes);

        List<Imports.Spec> functionList=new ArrayList<>();

        for(ImFunc imf:ImportFunction.functionList){
            functionList.add(imf.getFunc());
        }
        Imports imports = Imports.from(functionList, module);

        Instance instance = module.instantiate(imports);
        for(ImFunc imf:ImportFunction.functionList){
            imf.arInstance.set(instance);
        }
//        ImQueryBalanceByWalletId.arInstance.set(instance);

        return instance;
    }

    /**
     * 从内存中读出字符串
     * @param ptr 指针
     * @param mbf 内存
     * @return String
     */
    public static String getString(Integer ptr, ByteBuffer mbf) {
        StringBuilder sb = new StringBuilder();
        for(int i = ptr, max = mbf.limit(); i < max; i++) {
            mbf.position(i);
            byte b = mbf.get();
            if (b == 0) {
                break;
            }
            sb.appendCodePoint(b);
        }
        String result = sb.toString();
        return result;
    }
}
