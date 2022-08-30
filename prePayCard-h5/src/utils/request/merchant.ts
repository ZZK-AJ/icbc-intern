import request from "./request"

export const getMerchantById = (id: number | string) => {
    return request({
        method: "GET",
        url: "/merchantInfoById/" + id,
    });
}