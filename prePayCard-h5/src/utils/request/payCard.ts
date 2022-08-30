import request from "./request";

export const getPayCardByUserId = (userId: string) => {
    return request({
        method: "GET",
        url: "/payCard/user/" + userId,
        validateStatus: function (status) {
            return status >= 200 && status < 500; // 默认值
        }
    })
}
export const getMultiPayCardByUserId = (userId: string) => {
    return request({
        method: "GET",
        url: "/payCard/user/" + userId +"/multi",
        validateStatus: function (status) {
            return status >= 200 && status < 500; // 默认值
        }
    })
}
export const getPayCardById = (id: string | number) => {
    return request({
        method: "GET",
        url: "/pay/id/" + id,
    })
}


export const consume = (data: any, merchantId: number) => {
    if (isNaN(merchantId)) {
        return request({
            method: "POST",
            url: "/consume",
            data,
        })
    } else {
        return request({
            method: "POST",
            url: "/consume/" + merchantId,
            data,
        })
    }

}
export const getConsume = (consumeId: string | number) => {
    return request({
        method: "GET",
        url: "/consume/consumeId/" + consumeId,
    })
}
//查询消费记录
export const getRecord = (payCardId: string | number) => {
    return request({
        method: "GET",
        url: "/consume/id/" + payCardId,
    })
}

//查询余额
export const getBalance = (payCardId: string | number) => {
    return request({
        method: "GET",
        url: "/payCard/balance/" + payCardId,
    })
}

//退卡
export const refundPayCard = (payCardId: string | number, data: any) => {
    return request({
        method: "POST",
        url: "/refund/payedCardId/" + payCardId,
        data
    })
}

//充值
export const recharge = (data: any) => {
    return request({
        method: "POST",
        url: "/payCard/recharge",
        data
    })
}