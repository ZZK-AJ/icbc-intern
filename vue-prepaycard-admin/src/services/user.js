

import {MERCHANTLOGIN, MERCHANTID, MERCHANTSUBMITREVIEW, MERCHANTREVIEWRESULT, MERCHANTPAYEDCARDINFO, PAYEDCARDCONSUMEINFO} from '@/services/api'
import {REFUNDINFO, REFUNDPASS, REFUNDREJECT} from  '@/services/api'
import {OPERATORLOGIN, ROUTES} from '@/services/api'
import {request, METHOD, removeAuthorization} from '@/utils/request'

/**
 * 商户登录服务
 * @param merchantName 账户名
 * @param password 账户密码
 * @returns {Promise<AxiosResponse<T>>}
 */
export function merchantLogin(merchantName, password) {
  return request(MERCHANTLOGIN, METHOD.POST, {
    merchantName: merchantName,
    loginPasswd: password
  })
}

// 获取商户 id
export function getMerchantId(merchantName) {
  let reqUrl = MERCHANTID + merchantName
  // let auth = { 'Authorization': token }
  // let headers = {headers: auth}
  return request(reqUrl, METHOD.GET)  // 手动设置的 token 未生效
}


// 商户提交审核
export function merchantReview(merchant_id, card_name, card_type, card_info, card_amount, gift_amount, discount_rate, expire_date) {
  return request(MERCHANTSUBMITREVIEW, METHOD.POST, {
    merchantId: merchant_id,
    cardName: card_name,
    cardType: card_type,
    cardInfo: card_info,
    cardAmount: card_amount,
    giftAmount: gift_amount,
    discountRate: discount_rate,
    expireDate: expire_date
  })
}

// http://localhost:8080/review/merchantId/233
// 商家获取审核结果
export function getReviewResult(merchantId) {
  console.log(MERCHANTREVIEWRESULT)
  let reqUrl = MERCHANTREVIEWRESULT + merchantId
  console.log("===== getReviewResult " + reqUrl)
  return request(reqUrl, METHOD.GET)
}

// http://localhost:8080/merchant/payedCardInfo/{merchantId}
// 商家获取被购买的预付卡信息
export function getMerchantPayCardInfo(merchantId) {
  let reqUrl = MERCHANTPAYEDCARDINFO + merchantId
  return request(reqUrl, METHOD.GET)
}

// 商家获取被购买的预付卡信息
export function getPayedCardConsumeInfo(payCardId) {
  let reqUrl = PAYEDCARDCONSUMEINFO + payCardId
  return request(reqUrl, METHOD.GET)
}

// http://localhost:8080/refund/merchant/{merchantId}
// 商家获取退卡信息
export function getRefundInfo(merchantId) {
  let reqUrl = REFUNDINFO + merchantId
  return request(reqUrl, METHOD.GET)
}

// 商户通过退卡审核
export function refundPass(payedCardId) {
  let reqUrl = REFUNDPASS + payedCardId
  return request(reqUrl, METHOD.POST)
}

// 商户拒绝退卡审核
export function refundReject(payedCardId) {
  let reqUrl = REFUNDREJECT + payedCardId
  return request(reqUrl, METHOD.POST)
}


/**
 * 运营方登录服务
 * @param name 账户名
 * @param password 账户密码
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function operatorLogin(name, password) {
  return request(OPERATORLOGIN, METHOD.POST, {
    operatorName: name,
    loginPasswd: password
  })
}

/**
 * 获取路由
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function getRoutesConfig() {
  return request(ROUTES, METHOD.GET)
}

/**
 * 退出登录
 */
export function logout() {
  localStorage.removeItem(process.env.VUE_APP_ROUTES_KEY)
  localStorage.removeItem(process.env.VUE_APP_PERMISSIONS_KEY)
  localStorage.removeItem(process.env.VUE_APP_ROLES_KEY)
  removeAuthorization()
}
export default {
  merchantLogin,
  operatorLogin,
  logout,
  getRoutesConfig
}
