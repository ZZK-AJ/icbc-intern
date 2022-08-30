<template>
    <div class="nav">
        <navigation />
    </div>
    <div style="margin: 16px;">
        <van-cell-group title="支付金额">
            <van-cell center>
                <template #title>
                    <div class="money">
                        <span class="icon">￥</span>
                        <span class="number">{{ number }}</span>
                    </div>
                </template>
            </van-cell>
        </van-cell-group>
        <van-cell-group title="支付方式">
            <van-cell title="预付卡" is-link :value="cardName" @click="showSelect = true" />
        </van-cell-group>
        <van-cell-group title="收款方" v-if="merchant">
            <van-cell title="商户名称"  :value="merchant.merchantInfo" />
        </van-cell-group>
        <van-number-keyboard v-model="number" :show="true" z-index="0" theme="custom" :extra-key="['.', '00']"
            close-button-text="支付" :maxlength="10" @close="onClick" @blur="checkValue" />
    </div>
    <PayPassword v-model:show="showPassword" @finish="finishInput" />
    <SelectCard v-model:show="showSelect" :options="cardNames" @finish="finishSelect" />
</template>


<script lang="ts">
import { formatId } from '@/utils/format';
import { password } from '@/utils/md5';
import { getCardById } from '@/utils/request/card';
import { getMerchantById } from '@/utils/request/merchant';
import { consume, getPayCardById, getPayCardByUserId } from '@/utils/request/payCard';
import { Dialog } from 'vant';
import { defineComponent, onBeforeMount, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router';
import { useStore } from 'vuex';
import Navigation from '../Navigation.vue';
//自定义样式

export default defineComponent({
    setup() {
        const route = useRoute();
        const router = useRouter();
        const store = useStore();
        let payCardId = Number.parseInt(route.params.payCardId as string);
        const prePayCards = ref([]);
        let cardId = Number.parseInt(route.params.id as string);
        const merchantId = Number.parseInt(route.query.merchantId as string);
        const merchant = ref();

        const number = ref("")
        let val: number;
        const checkValue = () => {
            //处理输入
            val = parseFloat(number.value);
            val = parseFloat(val.toFixed(2));
            if (Number.isNaN(val) || val < 0) {
                number.value = "";
                return false;
            }
            if (val === 0) {
                val = 0.01;
            }

            number.value = val.toFixed(2);
            return true;
        }
        //确认支付流程
        const onClick = () => {
            if (checkValue()) {
                Dialog.confirm({
                    title: '确认支付',
                    message:
                        '￥' + number.value,
                }).then(() => {
                    showPassword.value = true;
                }).catch(() => {
                    // on cancel
                });
            } else {
                Dialog.alert({
                    title: '输入的金额无效',
                }).then(() => {

                });
            }

        }

        //密码
        const showPassword = ref(false);
        const finishInput = (payPasswd: string) => {
            consume({
                "id": 0,
                "payedCardId": payCardId,
                "payedAmount": val * 100,
                "payPasswd": password(payPasswd),
            }, merchantId).then(response => {
                if (response.data.code === 0) {
                    router.replace("/consume/id/" + response.data.data.id);
                } else {
                    Dialog.alert({
                        title: response.data.msg,
                    })
                }
            });
        }

        //校验

        const showSelect = ref(false);
        const cardNames: any = [];
        const cardName = ref("");
        let payCards: any = [];
        const user = store.state.user;

        onBeforeMount(async () => {
            const response = await getPayCardByUserId(user.userId);
            payCards = response.data.data.filter((item: any) => item.cardStatus === 0 && new Date(item.expireTime) > new Date());


            if (!isNaN(merchantId)) {
                //有这个参数就直接查询
                const response = await getMerchantById(merchantId);
                merchant.value = response.data.data;
                if (payCardId === 0) {
                    // const response = await getPayCardByUserId(user.userId);
                    payCards = payCards.filter((item: any) => item.merchantId === merchantId);
                    for (var i = 0; i < payCards.length; i++) {
                        const response = await getCardById(payCards[i].cardId);
                        cardNames.push(response.data.data.cardName + " (" + formatId(payCards[i].id).slice(-4) + ")");
                    }
                    if (cardNames.length === 0) {
                        Dialog.confirm({ title: "没有可用的预付卡", confirmButtonText: "立即购买" })
                            .then(() => {
                                store.commit("setCurrentIndex", 0);
                                router.replace("/?index=0");
                            }).catch(() => {
                                router.back();
                            });
                    } else {
                        showSelect.value = true;
                    }
                } else {
                    if (!payCards.map((item: any) => item.id).includes(payCardId)) {
                        console.log(payCards);
                        alert("参数错误");
                        
                        
                        router.back();
                    }
                    let response = await getPayCardById(payCardId);
                    const prePayCard = response.data.data;
                    cardId = prePayCard.cardId;
                    response = await getCardById(cardId);
                    const card = response.data.data;

                    cardNames.push(card.cardName + " (" + formatId(prePayCard.id).slice(-4) + ")");
                    cardName.value = cardNames[0];
                    payCards.push(prePayCard);
                }


            } else if (payCardId === 0) {
                //payCardId为0则找
                const response = await getPayCardByUserId(user.userId);
                payCards = response.data.data.filter((item: any) => item.cardId === cardId && item.cardStatus === 0 && new Date(item.expireTime) > new Date());
                for (var i = 0; i < payCards.length; i++) {
                    const response = await getCardById(payCards[i].cardId);
                    cardNames.push(response.data.data.cardName + " (" + formatId(payCards[i].id).slice(-4) + ")");
                }
                if (cardNames.length === 0) {
                    Dialog.confirm({ title: "没有可用的预付卡", confirmButtonText: "立即购买" })
                        .then(() => {
                            router.replace("/card/" + cardId);
                        }).catch(() => {
                            router.back();
                        });
                } else {
                    showSelect.value = true;
                }
            } else {
                const response = await getPayCardById(payCardId);
                const prePayCard = response.data.data;
                if (prePayCard.cardId != cardId) {
                    alert("参数错误");
                    router.back();
                } else {
                    const response = await getCardById(cardId);
                    const card = response.data.data;
                    cardNames.push(card.cardName + " (" + formatId(prePayCard.id).slice(-4) + ")");
                    cardName.value = cardNames[0];
                    payCards.push(prePayCard);
                }
            }

        });
        const finishSelect = (index: number) => {
            payCardId = payCards[index].id;
            cardName.value = cardNames[index];
        }
        return {
            number, showPassword, showSelect, prePayCards, cardNames, cardName,
            merchant,
            checkValue, onClick, finishInput, finishSelect,
        };
    },
    components: {
        Navigation,
    }
})
</script>

<style scoped>
.input {
    transform: scale(1, 2);
    background-color: black;
}

.money span {
    font-size: 40px;
    line-height: 40px;
    min-height: 40px;
}

.money .icon {
    font-size: 30px;
}
</style>