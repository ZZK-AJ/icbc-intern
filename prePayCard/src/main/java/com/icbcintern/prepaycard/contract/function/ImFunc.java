package com.icbcintern.prepaycard.contract.function;

import org.wasmer.Imports;
import org.wasmer.Instance;
import org.wasmer.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-07-29 15:14
 **/
public abstract class ImFunc {
    public String namespace = "env";
    public String name = "func";

    public ArrayList<Type> params = new ArrayList<>();
    public ArrayList<Type> results = new ArrayList<>();

    public AtomicReference<Instance> arInstance = new AtomicReference<>();


    public abstract void function(List<Number> argv);


    public Imports.Spec getFunc() {
        return new Imports.Spec(namespace, name, argv -> {
            function(argv);
            return argv;
        }, params, results);
    }
}
