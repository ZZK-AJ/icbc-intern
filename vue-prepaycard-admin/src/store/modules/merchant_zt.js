export default {
    namespaced: true,
    state: {
        merchantID: 0
    },
    getters: {
        merchantID: state => {
            state.merchantID = parseInt(localStorage.getItem("merchantId"))
            return state.merchantID
        }
    },
    mutations: {
        setMerchantID(state, value) {
            state.merchantID = value
            localStorage.setItem("merchantId", String(state.merchantID))
        }
    }
}
