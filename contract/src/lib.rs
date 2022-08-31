#![no_main]

use std::ffi::{CStr, CString};
use std::mem;
use std::os::raw::{c_char, c_void};
use json::{JsonValue, object};

//业务逻辑

#[link(wasm_import_module = "env")]
extern "C" {
    pub fn im_query_balance(wallet_id: *mut c_char) -> i64;
    pub fn im_transfer(x: *mut c_char, y: *mut c_char, z: i64) -> i64;
    pub fn im_insert_instance(x: i64, y: i64, z: i64) -> i64;
    pub fn sql_select_payed_card(id: i32, instance_id: i32) -> *mut c_char;
    pub fn sql_select_card(id: i32) -> *mut c_char;
    pub fn sql_select_instance(id: i32) -> *mut c_char;

    pub fn sql_update_instance(instance_json: *mut c_char) -> *mut c_char;

    pub fn sql_update_wallet(json_ptr: *mut c_char) -> i32;
    // pub fn sql_insert_contract_instance(merchant_wallet_id: i32, contract_wallet_id: i32, state: i32, discount: i32, gift_balance: i64, contract_id: i32) -> i32;
    pub fn sql_insert_contract_instance(json_ptr: *mut c_char) -> i32;
    pub fn im_query_wallet_id_by_card_id(card_id: i32) -> *mut c_char;
    pub fn sout(str: *mut c_char);
}

#[no_mangle]
extern fn recharge(instance_id: i32, money: i64) -> *mut c_char {
    let mut result = object! {
        "code" => 0,
        "success" => true,
        "message" => "success",
        "data" => object!{

            }
        };
    let result_str;
    unsafe {
        //获取冻结钱包id
        let result_instance = sql_select_instance(instance_id);
        let result_instance = CStr::from_ptr(result_instance).to_str().unwrap();
        let result_instance = json::parse(result_instance).unwrap();

        //获取预付卡id
        let result_payed_card = sql_select_payed_card(0, instance_id);
        let result_payed_card = CStr::from_ptr(result_payed_card).to_str().unwrap();
        let result_payed_card = json::parse(result_payed_card).unwrap();
        // let card_id = result_payed_card["id"].as_i32().unwrap();
        let card_status = result_payed_card["cardStatus"].as_i32().unwrap();
        if money <= 0 {
            result["success"] = JsonValue::from(false);
            result["message"] = JsonValue::from("Wrong parameter");
        } else if card_status != 0 {
            //卡已退
            result["success"] = JsonValue::from(false);
            result["message"] = JsonValue::from("Unavailable card");
        } else {
            let contract_wallet_id = result_instance["contractWalletId"].as_str().unwrap();
            let contract_wallet_ptr = CString::new(format!("{}", contract_wallet_id)).unwrap().into_raw();
            let balance = im_query_balance(contract_wallet_ptr);

            //更新余额
            let wallet = object! {
                "walletId" => contract_wallet_id,
                "balance" => balance+money,
                "type" => 3,
            };
            let json = CString::new(format!("{}", wallet.dump())).unwrap();
            let code = sql_update_wallet(json.into_raw());
            if code != 0 {
                result["success"] = JsonValue::from(false);
                result["message"] = JsonValue::from("Transaction failed");
            }
        }
    }
    result_str = result.dump();
    let string = CString::new(format!("{}", result_str)).unwrap();
    string.into_raw()
}

#[no_mangle]
extern fn sign_contract(contract_id: i32, payed_card_id: i32) -> i32 {
    unsafe {
        let result_payed_card = sql_select_payed_card(payed_card_id, 0);
        let result_payed_card = CStr::from_ptr(result_payed_card).to_str().unwrap();
        if result_payed_card.eq("null") {
            return -4;
        }
        let result_payed_card = json::parse(result_payed_card).unwrap();

        //获取合约钱包id
        let contract_wallet_id = result_payed_card["walletId"].as_str().unwrap();
        //获取cardId
        let card_id = result_payed_card["cardId"].as_i32().unwrap();
        let _card_status = result_payed_card["cardStatus"].as_i32().unwrap();


        let result_card = sql_select_card(card_id);
        let result_card = CStr::from_ptr(result_card).to_str().unwrap();
        if result_card.eq("null") {
            return -5;
        }
        let result_card = json::parse(result_card).unwrap();
        //获取初始金额
        //获取折扣信息
        let card_amount = result_card["cardAmount"].as_i64().unwrap();
        let gift_amount = result_card["giftAmount"].as_i64().unwrap();
        let discount_rate = result_card["discountRate"].as_i32().unwrap();
        //获取商户钱包id
        let merchant_wallet_id = result_card["walletId"].as_str().unwrap();
        //更新余额
        let wallet = object! {
            "walletId" => contract_wallet_id,
            "balance" => card_amount,
            "type" => 3,
        };
        let json = CString::new(format!("{}", wallet.dump())).unwrap();
        let code = sql_update_wallet(json.into_raw());
        if code < 0 {
            return code;
        }
        //创建实例
        let instance = object! {
            "merchantWalletId" => merchant_wallet_id,
            // "merchantWalletId" => "bug here",
            "contractWalletId" => contract_wallet_id,
            "state" => 0,
            "discount" => discount_rate,
            "giftBalance" => gift_amount,
            "contractId" => contract_id
        };
        let json = CString::new(format!("{}", instance.dump())).unwrap();
        sql_insert_contract_instance(json.into_raw())
        //返回实例id
    }
}

#[no_mangle]
extern fn sum(x: *mut c_char, y: *mut c_char) -> i32 {
    unsafe {
        let res = im_query_balance(x) + im_query_balance(y);
        res as i32
    }
}

#[no_mangle]
extern fn transfer(instance_id: i32, money: i64, wallet_id: *mut c_char) -> *mut c_char {
    unsafe {
        let mut result = object! {
        "code" => 0,
        "success" => true,
        "message" => "success",
        "data" => object!{

            }
        };
        let result_str;

        let result_instance = sql_select_instance(instance_id);
        let result_instance = CStr::from_ptr(result_instance).to_str().unwrap();
        let mut result_instance = json::parse(result_instance).unwrap();

        let from = result_instance["contractWalletId"].as_str().unwrap();
        let to = result_instance["merchantWalletId"].as_str().unwrap();
        let wallet_id = CStr::from_ptr(wallet_id).to_str().unwrap();

        let from_ptr = CString::new(format!("{}", from)).unwrap().into_raw();
        let to_ptr = CString::new(format!("{}", to)).unwrap().into_raw();

        let gift_balance = result_instance["giftBalance"].as_i64().unwrap();
        let discount = result_instance["discount"].as_i64().unwrap();
        if wallet_id.ne("DEFAULT") && wallet_id.ne(to) {
            result["success"] = JsonValue::from(false);
            result["message"] = JsonValue::from("Wrong WalletId");
        } else if money <= 0 {
            result["success"] = JsonValue::from(false);
            result["message"] = JsonValue::from("Wrong parameter");
        } else {
            let balance = im_query_balance(from_ptr);
            let discount_price: i64 = (money * discount - 1) / 100 + 1;
            if (balance + gift_balance) < discount_price {
                result["success"] = JsonValue::from(false);
                result["message"] = JsonValue::from("Not enough balance");
            } else {
                let mut gift = 0;
                let actual_price;
                if gift_balance > 0 {
                    //计算优惠 对半扣
                    if discount_price/2 > gift_balance {
                        gift = gift_balance;
                    } else {
                        gift = discount_price/2;
                    }
                    result_instance["giftBalance"] = JsonValue::from(gift_balance - gift);
                    let instance = result_instance.dump();
                    let instance_ptr = CString::new(format!("{}", instance)).unwrap().into_raw();
                    sql_update_instance(instance_ptr);

                    //计算优惠结束
                }
                actual_price = discount_price - gift;
                let mut code = 0;
                if actual_price > 0 {
                    code = im_transfer(from_ptr, to_ptr, actual_price);
                }
                if code != 0 {
                    result["code"] = JsonValue::from(code);
                    result["success"] = JsonValue::from(false);
                    result["message"] = JsonValue::from("Transaction failed");

                    //回滚数据
                    result_instance["giftBalance"] = JsonValue::from(gift_balance);
                    let instance = result_instance.dump();
                    let instance_ptr = CString::new(format!("{}", instance)).unwrap().into_raw();
                    sql_update_instance(instance_ptr);
                } else {
                    result["data"] = object! {
                        "originPrice"=>money,
                        "discountPrice"=>discount_price,
                        "gift"=>gift,
                        "actualPrice"=>actual_price
                    }
                }
            }
        }

        result_str = result.dump();
        let string = CString::new(format!("{}", result_str)).unwrap();
        string.into_raw()
    }
}

#[no_mangle]
extern fn refund(instance_id: i32) -> *mut c_char {
    unsafe {
        let mut result = object! {
        "code" => 0,
        "success" => true,
        "message" => "success",
        "data" => object!{

            }
        };
        let result_str;


        //获取冻结钱包id
        let result_instance = sql_select_instance(instance_id);
        let result_instance = CStr::from_ptr(result_instance).to_str().unwrap();
        let result_instance = json::parse(result_instance).unwrap();

        //获取预付卡id
        let result_payed_card = sql_select_payed_card(0, instance_id);
        let result_payed_card = CStr::from_ptr(result_payed_card).to_str().unwrap();
        let result_payed_card = json::parse(result_payed_card).unwrap();
        let card_id = result_payed_card["id"].as_i32().unwrap();

        let from = result_instance["contractWalletId"].as_str().unwrap();

        //获取用户钱包id以退款
        let result_user_card = im_query_wallet_id_by_card_id(card_id);
        let to = match CStr::from_ptr(result_user_card).to_str() {
            Ok(to) => { to }
            Err(_) => {
                sout(result_user_card);
                ""
            }
        };
        //查询余额
        let from_ptr = CString::new(format!("{}", from)).unwrap().into_raw();
        let to_ptr = CString::new(format!("{}", to)).unwrap().into_raw();
        let balance = im_query_balance(from_ptr);
        let to_balance = im_query_balance(to_ptr);


        if balance <= 0 {
            result["success"] = JsonValue::from(false);
            result["message"] = JsonValue::from("Not enough balance");
        } else {
            if to_balance < 0 {
                result["success"] = JsonValue::from(false);
                result["message"] = JsonValue::from("Unavailable payment account");
            } else {
                let code = im_transfer(from_ptr, to_ptr, balance);
                if code != 0 {
                    result["code"] = JsonValue::from(code);
                    result["success"] = JsonValue::from(false);
                    result["message"] = JsonValue::from("Transaction failed");
                } else {
                    result["data"] = object! {
                        "refundAmount"=>balance,
                    }
                }
            }
        }
        result_str = result.dump();
        let string = CString::new(format!("{}", result_str)).unwrap();
        string.into_raw()
    }
}

#[no_mangle]
extern fn get_balance_by_wallet_id(wallet_id: *mut c_char) -> i64 {
    unsafe {
        let balance = im_query_balance(wallet_id);
        return balance;
    }
}

#[no_mangle]
extern fn get_balance(instance_id: i32) -> i64 {
    unsafe {
        //获取冻结钱包id
        let result_instance = sql_select_instance(instance_id);
        let result_instance = CStr::from_ptr(result_instance).to_str().unwrap();
        let result_instance = json::parse(result_instance).unwrap();
        let wallet_id = result_instance["contractWalletId"].as_str().unwrap();
        let wallet_ptr = CString::new(format!("{}", wallet_id)).unwrap().into_raw();
        let balance = im_query_balance(wallet_ptr);
        balance
    }
}

#[no_mangle]
extern fn get_gift_balance(instance_id: i32) -> i64 {
    unsafe {
        //获取冻结钱包id
        let result_instance = sql_select_instance(instance_id);
        let result_instance = CStr::from_ptr(result_instance).to_str().unwrap();
        let result_instance = json::parse(result_instance).unwrap();
        let gift_balance = result_instance["giftBalance"].as_i64().unwrap();
        gift_balance
    }
}
//字符串方法封装
// extern {
//     // fn get_string_len_from_java(str_ptr: *mut c_char) -> i32;
//     // fn get_string_from_java(key_ptr: *mut c_char, value_ptr: *mut c_char) -> *mut c_char;
//     pub fn im_query_balance_by_wallet_id(key_ptr: *mut c_char) -> *mut c_char;
// }
//
// #[no_mangle]
// pub extern fn get_wallet_by_id(param_ptr: *mut c_char) -> *mut c_char {
//     let result_ptr;
//     unsafe {
//         result_ptr = im_query_balance_by_wallet_id(param_ptr);
//     }
//     let result;
//     unsafe {
//         result = CStr::from_ptr(result_ptr).to_str().unwrap();
//     }
//
//     let string = CString::new(format!("wallet:{}", result)).unwrap();
//     string.into_raw()
// }

#[no_mangle]
pub extern fn allocate(size: usize) -> *mut c_void {
    let mut buffer = Vec::with_capacity(size);
    let ptr = buffer.as_mut_ptr();
    mem::forget(buffer);

    ptr as *mut c_void
}

//释放刚刚申请的用于存放参数的内存
#[no_mangle]
pub extern fn deallocate(ptr: *mut c_void, capacity: usize) {
    unsafe {
        let _ = Vec::from_raw_parts(ptr, 0, capacity);
    }
}

//拿回返回值所有权,销毁生命周期?
#[no_mangle]
pub extern fn drop_string(ptr: *mut c_char) {
    unsafe {
        let _ = CString::from_raw(ptr);
    }
}