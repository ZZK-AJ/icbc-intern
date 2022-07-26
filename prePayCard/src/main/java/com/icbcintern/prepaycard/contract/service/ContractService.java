package com.icbcintern.prepaycard.contract.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icbcintern.prepaycard.contract.Aop.ContractLog;
import com.icbcintern.prepaycard.contract.dao.ContractDao;
import com.icbcintern.prepaycard.contract.pojo.Contract;
import com.icbcintern.prepaycard.contract.pojo.ContractInstance;
import com.icbcintern.prepaycard.contract.utils.InstanceUtils;
import com.icbcintern.prepaycard.contract.utils.StringUtils;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wasmer.Instance;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-08-01 11:25
 **/
@Service
public class ContractService {
    @Autowired
    ContractDao contractDao;

    public static HashMap<String, String> ERROR_MESSAGES = new HashMap<>();

    static {
        ERROR_MESSAGES.put("Transaction failed", "交易失败");
        ERROR_MESSAGES.put("Unavailable card", "预付卡不可用");
        ERROR_MESSAGES.put("Unavailable payment account", "退款账户不可用");
        ERROR_MESSAGES.put("Not enough balance", "预付卡余额不足");
        ERROR_MESSAGES.put("Wrong parameter", "支付参数错误");
        ERROR_MESSAGES.put("Wrong WalletId","此预付卡暂不支持在该商户消费");
        ERROR_MESSAGES.put("success", "执行成功");
    }

    public static String ERROR_MESSAGE(String key) {
        return ERROR_MESSAGES.getOrDefault(key, "未知错误");
    }

    public List<Contract> getAllContract() {
        return contractDao.getAllContract();
    }

    public List<Contract> getValid() {
        return contractDao.getValidContract();
    }

    @ContractLog
    public boolean destroyInstance(Integer contractInstanceId) {
        ContractInstance contractInstance = contractDao.getContractInstanceById(contractInstanceId);
        contractInstance.setState(1);
        int effectNum = contractDao.updateContractInstance(contractInstance);
        return effectNum > 0;
    }

    @ContractLog
    public int deploy(Contract contract) {
        contractDao.insertContract(contract);
        return contract.getContractId();
    }

    @ContractLog
    public boolean destroy(Integer contractId) {
        Contract contract = contractDao.getContractById(contractId);
        if (contract == null) return false;
        contract.setState(1);
        int effectNum = contractDao.updateContract(contract);
        return effectNum > 0;
    }

    /**
     * 若返回值小于0，则代表生成失败 -1:钱包不存在 -2:钱包非空 -3:合约已被销毁 -4:payedCardId不存在 -5:对应Card数据不存在
     * @param payedCardId 预付卡购买表中的id
     * @param contractId 合约id
     * @return 生成的合约实例id
     * @throws Exception 合约路径无效导致的异常
     */
    @ContractLog
    public int signContract(int payedCardId, int contractId) throws Exception {
        Contract contract = contractDao.getContractById(contractId);
        int state = contract.getState();
        if (state == 1) {
            return -3;
        }
        String path = contract.getPath();
        File file = new File(path);
        String wasmPaths = file.toURI().toString();
        //TODO 加密或校验合约内容
        Instance instance = InstanceUtils.getWasmInstance(wasmPaths);
        Object[] results = instance.exports.
                getFunction("sign_contract").apply(contractId, payedCardId);
        Integer instanceId = (Integer) results[0];
        return instanceId;
    }

    /**
     *
     * @param contractInstanceId 合约实例id
     * @param money 充值金额
     * @return 执行结果
     * @throws Exception 数据解析异常
     */
    @ContractLog
    public Result recharge(int contractInstanceId, long money) throws Exception {
        Instance instance = null;
        try {
            instance = getWasmInstanceByContractInstanceId(contractInstanceId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(1, e.getMessage(), null);
        }
        StringUtils stringUtils = new StringUtils(instance);
        Object[] results = instance.exports.
                getFunction("recharge").apply(contractInstanceId, money);
        Integer ptr = (Integer) results[0];
        String json = stringUtils.getString(ptr);
        return parseJson(json);
    }

    /**
     * @param contractInstanceId 合约实例id
     * @param money              付款金额
     * @param walletId
     * @return 执行结果
     * @throws Exception 数据解析异常
     */
    @ContractLog
    public Result transfer(int contractInstanceId, long money, String walletId) throws Exception {
        Instance instance = null;
        try {
            instance = getWasmInstanceByContractInstanceId(contractInstanceId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(1, e.getMessage(), null);
        }
        StringUtils stringUtils = new StringUtils(instance);
        //将walletId传进去
        if(walletId==null){
            walletId = "DEFAULT";
        }
        int walletIdPtr = stringUtils.addString(walletId);
        Object[] results = instance.exports.
                getFunction("transfer").apply(contractInstanceId, money,walletIdPtr);
        Integer ptr = (Integer) results[0];
        String json = stringUtils.getString(ptr);
        return parseJson(json);
    }

    /**
     *
     * @param contractInstanceId 合约实例id
     * @return 执行结果 code=0:执行成功 code=-1:执行失败
     * @throws Exception 数据解析异常
     */
    @ContractLog
    public Result refund(int contractInstanceId) throws Exception {
        Instance instance = null;
        try {
            instance = getWasmInstanceByContractInstanceId(contractInstanceId);
        } catch (Exception e) {
            return new Result(1, e.toString(), null);
        }
        StringUtils stringUtils = new StringUtils(instance);
        Object[] results = instance.exports.
                getFunction("refund").apply(contractInstanceId);
        Integer ptr = (Integer) results[0];
        String json = stringUtils.getString(ptr);
        return parseJson(json);
    }


    @ContractLog
    public Long getBalance(int contractInstanceId) {
        Instance instance = null;
        try {
            instance = getWasmInstanceByContractInstanceId(contractInstanceId);
        } catch (Exception e) {
            return -1L;
        }
        Object[] results = instance.exports.
                getFunction("get_balance").apply(contractInstanceId);

        return (Long) results[0];
    }
    @ContractLog
    public Long getGiftBalance(int contractInstanceId) {
        Instance instance = null;
        try {
            instance = getWasmInstanceByContractInstanceId(contractInstanceId);
        } catch (Exception e) {
            return -1L;

        }
        Object[] results = instance.exports.
                getFunction("get_gift_balance").apply(contractInstanceId);

        return (Long) results[0];
    }

    //根据contractInstanceId获取wasmInstance
    private Instance getWasmInstanceByContractInstanceId(int contractInstanceId) throws Exception {
        ContractInstance contractInstance = contractDao.getContractInstanceById(contractInstanceId);
        if (contractInstance == null) {
            throw new Exception("合约实例不存在");
        }
        Contract contract = contractDao.getContractById(contractInstance.getContractId());
        if (contract == null) {
            throw new Exception("合约不存在");
        }
        int state = contract.getState();
        if (state == 1) {
            throw new Exception("合约已销毁");
        }
        String path = contract.getPath();
        File file = new File(path);
        String wasmPaths = file.toURI().toString();

        return InstanceUtils.getWasmInstance(wasmPaths);
    }

    private Result parseJson(String json) throws JsonProcessingException {
        ObjectMapper jackson = new ObjectMapper();

        HashMap map = jackson.readValue(json, HashMap.class);
        Result result = new Result((Integer) map.get("code"), ERROR_MESSAGE((String) map.get("message")), map.get("data"));
        if (result.getCode() == 0 && !((boolean) map.get("success"))) {
            result.setCode(-1);
        }
        return result;
    }


}