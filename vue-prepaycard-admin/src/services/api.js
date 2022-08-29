// 跨域代理前缀
// const {getRefundInfo} = require("@/services/user");
// const API_PROXY_PREFIX='/api'
// const BASE_URL = process.env.NODE_ENV === 'production' ? process.env.VUE_APP_API_BASE_URL : API_PROXY_PREFIX
const BASE_URL = '/api'

// const BASE_URL = process.env.VUE_APP_API_BASE_URL
module.exports = {
  // 商户登录
  MERCHANTLOGIN: `${BASE_URL}/merchantLogin`,
  // 获取商家id
  MERCHANTID: `${BASE_URL}/merchantInfoByName/`,
  // 商户提交审核
  MERCHANTSUBMITREVIEW: `${BASE_URL}/review/submit`,
  // 商户获取审核结果
  MERCHANTREVIEWRESULT: `${BASE_URL}/review/merchantId/`,
  // 获取商户被购买的所有预付卡
  MERCHANTPAYEDCARDINFO: `${BASE_URL}/merchant/payedCardInfo/`,
  // 获取预付卡消费记录
  PAYEDCARDCONSUMEINFO: `${BASE_URL}/consume/id/`,

  // 商户获取退卡信息
  REFUNDINFO: `${BASE_URL}/refund/merchant/`,
  // 商户通过退卡
  REFUNDPASS: `${BASE_URL}/refund/pass/`,
  // 商户拒绝退卡
  REFUNDREJECT: `${BASE_URL}/refund/reject/`,

  OPERATORLOGIN: `${BASE_URL}/operatorLogin`,
  // 从后端获取了路由
  ROUTES: `${BASE_URL}/routes`
  // 根据需要自行添加路由

}
