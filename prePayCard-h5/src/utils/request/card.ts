import request from "./request"

export const getAllCard = () => {
    return request({
        method: "GET",
        url: "/card/all",
    })
}

export const getCardById = (id: string|number) => {
    return request({
        method: "GET",
        url: "/card/" + id,
    })
}

export const buyCardById = (id: string) => {
    return request({
        method: "POST",
        url: "/pay/payedCard/" + id,
    })
}