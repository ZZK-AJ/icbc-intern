package com.icbcintern.prepaycard.contract.controller;

import com.icbcintern.prepaycard.contract.pojo.Contract;
import com.icbcintern.prepaycard.contract.service.ContractService;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-08-02 11:05
 **/
@RestController
public class ContractController {
    @Value("${contract.dir}")
    private String dir;

    @Autowired
    ContractService contractService;

    @GetMapping("/contract/all")
    public Result getAll() {
        List<Contract> contracts = contractService.getAllContract();
        Result result = Result.ok();
        result.setData(contracts);
        return result;
    }

    @GetMapping("/contract/valid")
    public Result getValid() {
        List<Contract> contracts = contractService.getValid();
        Result result = Result.ok();
        result.setData(contracts);
        return result;
    }

    @PostMapping("/contract/upload")
    public Result deploy(String name, MultipartFile file) {
        String originName = file.getOriginalFilename();
        String extName = originName.substring(originName.lastIndexOf("."));
        boolean isok = extName.toLowerCase(Locale.ROOT).matches(".(wasm|WASM|)$");
        if (!isok) {
            return new Result(1, "文件格式错误", null);
        }
        File newFile = new File(dir);
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
        String fileName = UUID.randomUUID().toString() + extName;
        try {
            file.transferTo(new File(dir + fileName));

        } catch (Exception e) {
            return new Result(1, "文件上传失败", null);
        }
        Contract contract = new Contract(0, dir + fileName, name, 0);
        contractService.deploy(contract);
        Result result = Result.ok();
        result.setData(contract);
        return result;
    }

    @PostMapping("/contract/destroy/{contractId}")
    public Result destroy(@PathVariable Integer contractId) {
        if (contractService.destroy(contractId)) {
            return Result.ok();
        }
        return new Result(1, "销毁失败", null);
    }
}
