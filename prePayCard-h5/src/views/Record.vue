<template>
    <div class="nav">
        <Navigation />
        <Navigation title="消费记录" :fixed="true" />
    </div>
    <div class="record">
        <van-dropdown-menu v-if="cards">
            <van-dropdown-item v-model="payCardId" :options="option" @change="getData(payCardId)" />
        </van-dropdown-menu>
        <van-cell-group title="消费记录">
            <div v-for="record in records.slice(perPage * (currentPage - 1), perPage * (currentPage))">
                <van-cell size="large" :to="'/consume/id/' + record.id">
                    <template #title>
                        <div class="success" v-if="record.state === 0">
                            交易成功
                        </div>
                        <div class="failed" v-if="record.state !== 0">
                            交易失败
                        </div>
                    </template>
                    <template #value>
                        <span class="custom-title">{{ money(record.payedAmount) }}</span>
                    </template>
                    <template #label>
                        <span class="custom-title">{{ time(record.payedTime) }}</span>
                    </template>
                </van-cell>
            </div>
        </van-cell-group>
    </div>
    <div class="pagination">
        <van-config-provider :theme-vars="myPagination">
            <van-pagination v-model="currentPage" :total-items="records.length" :items-per-page="perPage" />
        </van-config-provider>
    </div>
    <van-number-keyboard safe-area-inset-bottom />
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import Navigation from '@/components/Navigation.vue';

import { getPayCardByUserId, getRecord } from '@/utils/request/payCard';
import { getCardById } from '@/utils/request/card';
import { formatMoney, transformTimestamp } from "@/utils/format"
import { useStore } from 'vuex';
import { useRoute } from 'vue-router';
//自定义样式
const myPagination = {
    paginationItemDefaultColor: "#ee0a24",
};

const currentPage = ref(1);
const perPage = ref(10);

// 查询数据
// 获取消费记录
const records: any = ref([]);
const getData = (payCardId: string | number) => {
    getRecord(payCardId).then(response => {
        records.value = (response.data.data).reverse();
        currentPage.value = 1;
    })
};

const time = (str: string) => transformTimestamp(str);
const money = (money: number) => "￥" + formatMoney(money);

export default defineComponent({
    setup() {
        const route = useRoute();
        const params:number = route.query.payCardId?parseInt(route.query.payCardId+""):-1;
        
        const payCardId = ref(params);
        const option = ref([])
        const cards = ref(null);
        const cardsInfo: any = [];



        const store = useStore();
        const user = store.state.user;

        // 获取用户持有的卡,并把信息查询出来
        // TODO 通过缓存获取,每次都重新请求太慢了
        getPayCardByUserId(user.userId).then(response => {
            if (response.data.code === 0) {
                option.value = response.data.data.map((item: { cardId: number; id: number; }) => {
                    const cardName = ref("");
                    if (payCardId.value == -1) payCardId.value = item.id;
                    getCardById(String(item.cardId)).then(response => {
                        if (response.data.code === 0 && response.data.data) {
                            cardName.value = response.data.data.cardName;
                            cardsInfo.push(response.data.data);
                        }
                    }).catch(error => { });
                    return { text: cardName, value: item.id }
                });
                cards.value = response.data.data;
            }
            getData(payCardId.value);
        }).catch(error => {
            console.log(error)
        })


        return {
            payCardId, option, currentPage, perPage, myPagination, cards, cardsInfo, records,
            getData, time, money
        };
    },
    components: { Navigation }
})
</script>

<style scoped>
.record {
    min-height: 80vh;
    --van-nav-bar-icon-color: var(--van-danger-color);
    --van-nav-bar-text-color: var(--van-danger-color);
}

.failed {
    color: #ee0a24;
}

/* .pagination {
    position: fixed;
    bottom: 0px;
    margin: 10px auto;
    min-width: 375px;
    min-height: 20px;
} */
</style>