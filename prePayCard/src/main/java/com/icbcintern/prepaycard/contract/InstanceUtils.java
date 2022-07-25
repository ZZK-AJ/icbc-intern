package com.icbcintern.prepaycard.contract;

import com.icbcintern.prepaycard.contract.function.ImportFunction;
import org.wasmer.Imports;
import org.wasmer.Instance;
import org.wasmer.Module;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * description:wasm加载与函数导入
 * @author: He Yihui
 * @create: 2022-07-25 10:19
 **/
public class InstanceUtils {
    public static Instance getWasmInstance(String wasmPaths) throws Exception {
        System.out.println(wasmPaths);

        Path wasmPath = Paths.get(new URI(wasmPaths));


        // Reads the WebAssembly module as bytes.
        byte[] wasmBytes = Files.readAllBytes(wasmPath);

        // Instantiates the WebAssembly module.
//        Instance instance = new Instance(wasmBytes);

        Module module = new Module(wasmBytes);


        Imports imports = Imports.from(ImportFunction.functionList, module);
        Instance instance = module.instantiate(imports);

        return instance;

    }
}
