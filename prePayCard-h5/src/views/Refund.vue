<template>
    <div class="nav">
        <navigation title="退卡记录" />
    </div>
    <div>
        <van-tabs v-model:active="active" swipeable>
            <van-tab :title="'退卡中(' + (refundingPayCards ? refundingPayCards.length : 0) + ')'">
                <van-cell-group title="预付卡">
                    <div v-for="payCard in refundingPayCards">
                        <van-cell size="large" :to="'/mycard/' + payCard.payedCard.cardId + '/' + payCard.payedCard.id">
                            <template #title>
                                <div class="success">
                                    {{ payCard.card.cardName }}
                                </div>
                            </template>
                            <template #value>
                                <span class="custom-title">退卡中</span>
                            </template>
                            <template #label>
                                <span class="custom-title">{{ formatId(payCard.payedCard.id) }}</span>
                            </template>
                        </van-cell>
                    </div>
                </van-cell-group>
                <div class="empty">
                    <van-empty v-if="refundingPayCards && refundingPayCards.length == 0" description="无记录" />
                </div>
            </van-tab>
            <van-tab :title="'已退卡(' + (refundedPayCards ? refundedPayCards.length : 0) + ')'">
                <van-cell-group title="预付卡">
                    <div v-for="payCard in refundedPayCards">
                        <van-cell size="large" :to="'/mycard/' + payCard.payedCard.cardId + '/' + payCard.payedCard.id">
                            <template #title>
                                <div class="success">
                                    {{ payCard.card.cardName }}
                                </div>
                            </template>
                            <template #value>
                                <span class="custom-title">已退卡</span>
                            </template>
                            <template #label>
                                <span class="custom-title">{{ formatId(payCard.payedCard.id) }}</span>
                            </template>
                        </van-cell>
                    </div>
                </van-cell-group>
                <div class="empty">
                    <van-empty v-if="refundedPayCards && refundedPayCards.length == 0" description="无记录" />
                </div>
            </van-tab>
        </van-tabs>
    </div>
</template>

<script setup lang="ts">
import { formatId } from '@/utils/format';
import { getMultiPayCardByUserId, getPayCardByUserId } from '@/utils/request/payCard';
import { ref } from 'vue';
import { useStore } from 'vuex';

const active = ref(0);
const refundingPayCards = ref();
const refundedPayCards = ref();
const store = useStore();
const user = store.state.user;
getMultiPayCardByUserId(user.userId).then(response => {
    if (response.data.code === 0) {
        refundingPayCards.value = response.data.data.filter((item: any) => item.payedCard.cardStatus == 1);
        refundedPayCards.value = response.data.data.filter((item: any) => item.payedCard.cardStatus == 2);
    }
}).catch(error => {

})
</script>

<style scoped>
</style>