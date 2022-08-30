<template>
    <div class="nav">
        <van-nav-bar @click-left="onClickLeft" @click-right="show = true">
            <template #title>
                <span class="title" style=" font-weight: bold;">预付卡详情</span>
            </template>
            <template #left>
                <van-icon name="arrow-left" size="0.48rem" color="#ee0a24" />
            </template>
            <template #right>
                <span class="title" style=" font-weight: bold;color: #ee0a24">···</span>
            </template>
        </van-nav-bar>
    </div>
    <div v-if="card && payCard">
        <div class="card">
            <Card :title="card.cardName" :label="'余额:' + money" :disable="cardRefunded || cardExpired || cardRefunding"
                :value="cardRefunded ? '已退卡' : cardRefunding ? '退卡中' : cardExpired ? '已过期' : ''"></Card>

        </div>
        <div style="margin: 16px;">
            <van-row>
                <!-- <van-col span="2"></van-col> -->
                <van-col span="11">
                    <van-button :disabled="cardRefunded || cardExpired || cardRefunding" round block type="primary"
                        color="#ee0a24" @click="onClick">
                        直接付款
                    </van-button>
                </van-col>
                <van-col span="2"></van-col>
                <van-col span="11">
                    <van-button :disabled="cardRefunded || cardExpired || cardRefunding" round block type="primary"
                        color="#ee0a24" @click="onClick2">
                        扫码付款
                    </van-button>
                </van-col>
            </van-row>
        </div>
        <div class="recharge info">
            <div class="title">充值面额</div>
            <van-row gutter="20">
                <van-col span="8">
                    <van-button block plain type="danger" @click="onRecharge(100)"
                        :disabled="cardRefunded || cardExpired || cardRefunding">￥100</van-button>
                </van-col>
                <van-col span="8">
                    <van-button block plain type="danger" @click="onRecharge(200)"
                        :disabled="cardRefunded || cardExpired || cardRefunding">￥200</van-button>
                </van-col>
                <van-col span="8">
                    <van-button block plain type="danger" @click="onRecharge(500)"
                        :disabled="cardRefunded || cardExpired || cardRefunding">￥500</van-button>
                </van-col>
            </van-row>
        </div>
        <div v-if="card && payCard" class="info">
            <div class="van-ellipsis title">{{ card.cardName }}</div>
            <van-row>
                <van-col span="8">
                    <p>卡号：</p>
                </van-col>
                <van-col span="16">
                    <p>{{ payCardIdFormat }}</p>
                </van-col>
                <van-col span="8">
                    <p>智能合约编号：</p>
                </van-col>
                <van-col span="16">
                    <p>{{ payCard.instanceId }}</p>
                </van-col>
                <van-col span="8">
                    <p>子钱包编号：</p>
                </van-col>
                <van-col span="16">
                    <div class="van-ellipsis">
                        <p>{{ payCard.walletId }}</p>
                    </div>
                </van-col>
                <van-col span="8">
                    <p>初始金额：</p>
                </van-col>
                <van-col span="16">
                    <p>{{ (card.cardAmount / 100).toFixed(2) }}</p>
                </van-col>
                <van-col span="8">
                    <p>余额：</p>
                </van-col>
                <van-col span="16">
                    <p>{{ (balance / 100).toFixed(2) }}</p>
                </van-col>
                <van-col span="8">
                    <p>卡赠送余额：</p>
                </van-col>
                <van-col span="16">
                    <p>{{ (giftBalance / 100).toFixed(2) }}</p>
                </van-col>
                <van-col span="8">
                    <p>折扣</p>
                </van-col>
                <van-col span="16">
                    <p v-if="card.discountRate<100">{{card.discountRate/10}}折</p>
                    <p v-if="card.discountRate>=100">无折扣</p>
                </van-col>
                <van-col span="8">
                    <p>有效期：</p>
                </van-col>
                <van-col span="16">
                    <p>{{ from_date }} 至 {{ to_date }}</p>
                </van-col>
            </van-row>

        </div>
    </div>

    <div class="actionSheet">
        <van-action-sheet v-model:show="show" :actions="actions" cancel-text="取消" close-on-click-action
            @select="onSelect" />
    </div>

    <PayPassword v-model:show="showPassword" @finish="finishInput" />
    <PayPassword v-model:show="showPassword2" @finish="finishInput2" />
</template>


<script lang="ts">
import { formatDateFromTimestamp, formatId, formatMoney, transformTimestamp } from '@/utils/format';
import { password } from '@/utils/md5';
import { getCardById } from '@/utils/request/card';
import { getBalance, getPayCardById, recharge, refundPayCard } from '@/utils/request/payCard';
import { Dialog, Toast } from 'vant';
import { defineComponent, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router';
import Navigation from '../Navigation.vue';

const onClickLeft = () => history.back();

export default defineComponent({
    setup() {
        const route = useRoute();
        const router = useRouter();
        let id = route.params.id as string;
        let payCardId = route.params.payCardId as string;
        const payCardIdFormat = formatId(payCardId);
        const payCard = ref();
        //预付卡状态
        const cardRefunded = ref(false);
        const cardRefunding = ref(false);
        const cardExpired = ref(false);
        const card = ref();
        const balance = ref(0);
        const giftBalance = ref(0);
        const money = ref("");
        const from_date = ref("1970-01-01 ")
        const to_date = ref("1970-01-01 ")
        getPayCardById(payCardId).then(response => {
            if (response.data.code === 0 && response.data.data) {
                response.data.data.instanceId = formatId(response.data.data.instanceId);
                payCard.value = response.data.data;
                from_date.value = formatDateFromTimestamp(response.data.data.payTime);
                to_date.value = formatDateFromTimestamp(response.data.data.expireTime);
                const now = new Date();
                const expire = new Date(response.data.data.expireTime);

                if (payCard.value.cardStatus == 2) {
                    cardRefunded.value = true;
                }
                else if (payCard.value.cardStatus == 1) {
                    cardRefunding.value = true;
                }
                if (expire < now) {
                    cardExpired.value = true;
                }
            }
        })

        getCardById(id).then(response => {
            if (response.data.code === 0 && response.data.data) {
                card.value = response.data.data;

            }
            else {
            }
        }).catch(error => { });

        getBalance(payCardId).then(response => {
            if (response.data.code === 0) {
                balance.value = response.data.data[0];
                giftBalance.value = response.data.data[1];
                money.value = formatMoney(balance.value + giftBalance.value);
            }
        });
        //支付按钮
        const onClick = () => {
            const path = route.path;
            router.push(path + "/pay")
        }
        const onClick2 = () => {

            router.push("/scan?from=" + payCardId);
        }
        //退卡
        const refund = () => {
            Dialog.confirm({
                title: '确认退还预付卡?',
                message: "退卡后预付卡将暂时无法消费\n剩余余额将在商户处理后原路退还"
            }).then(() => {
                console.log("退卡" + payCardId);
                showPassword.value = true;
            }).catch(() => {
            });

        }
        //充值
        let rechargeMoney = 0;
        const onRecharge = (_money: number) => {
            rechargeMoney = _money * 100;
            Dialog.confirm({
                title: '确认充值',
                message: "￥" + formatMoney(rechargeMoney),
            }).then(() => {
                console.log("充值" + payCardId);
                showPassword2.value = true;
            }).catch(() => {
            });
        }

        //显示更多
        const show = ref(false);
        const actions = ref([

            { name: '消费记录' },
            { name: '退卡', disabled: cardRefunded, color: "#ee0a24" },

        ]);
        const onSelect = (item: any) => {
            // 默认情况下点击选项时不会自动收起
            // 可以通过 close-on-click-action 属性开启自动收起
            switch (item.name) {
                case '退卡':
                    refund();
                    break;
                case '消费记录':
                    router.push("/record?payCardId=" + payCardId);
                    break;

            }

        };

        //密码
        const showPassword = ref(false);
        const finishInput = (payPasswd: string) => {
            refundPayCard(payCardId, { 'payPasswd': password(payPasswd) }).then(response => {
                if (response.data.code === 0) {
                    Dialog.alert({
                        title: "退卡成功"
                    }).then(() => {
                        window.location.reload();
                    })
                } else {
                    Dialog.alert({
                        title: "退卡失败",
                        message: "请稍后重试"
                    }).then(() => {
                        window.location.reload();
                    })
                }
            })
        }
        const showPassword2 = ref(false);
        const finishInput2 = (payPasswd: string) => {
            recharge({ 'payedCardId': payCardId, 'money': rechargeMoney, 'payPasswd': password(payPasswd) }).then(response => {
                if (response.data.code === 0) {
                    Dialog.alert({
                        title: "充值成功",
                        message: "￥" + formatMoney(rechargeMoney),
                    }).then(() => {
                        window.location.reload();
                    })
                } else {
                    Dialog.alert({
                        title: "充值失败",
                        message: "请稍后重试"
                    }).then(() => {
                        window.location.reload();
                    })
                }
            })
        }
        return {
            card, payCard, cardRefunded, cardRefunding, cardExpired,
            balance, giftBalance, money, payCardId, payCardIdFormat, from_date, to_date,
            onClick, onClick2, onRecharge, onClickLeft, finishInput, finishInput2,
            show, actions, onSelect,
            showPassword, showPassword2,
        };
    },
    components: {
        Navigation,
    }
})
</script>

<style scoped>
．pay {
    margin: 16px;
}

.card {
    margin-top: 30px;

}

.info {
    width: 350px;
    margin: 0 auto;
}

.info .title {
    margin-top: 10px;
    font-size: medium;
}

.info p {
    color: gray;
    font-size: small;
    margin: 0;
    padding: 0;
}
</style>