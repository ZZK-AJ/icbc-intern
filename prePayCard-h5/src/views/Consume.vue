<template>
    <div>
        <div class="title">
            {{ title }}
        </div>
        <van-cell-group inset title="支付信息">
            <van-cell title="实付金额" :value="actualPrice"></van-cell>
            <van-cell title="原价" :value="originPrice"></van-cell>
            <!-- <van-cell title="折扣价" :value="discountPrice"></van-cell> -->
            <!-- <van-cell title="优惠" :value="gift"></van-cell> -->
            <van-collapse v-model="activeNames">
                <van-collapse-item title="优惠" name="1" :value="sub">
                    <van-cell title="折扣" :value="discount"></van-cell>
                    <van-cell title="赠金" :value="gift"></van-cell>
                </van-collapse-item>
            </van-collapse>
        </van-cell-group>
        <van-cell-group inset title="订单信息">
            <van-cell title="订单编号" :value="consumeIdFomat"></van-cell>
            <van-cell title="支付时间" :value="payedTime"></van-cell>
            <van-cell title="支付方式" value="在线支付"></van-cell>
        </van-cell-group>
    </div>
</template>

<script setup lang="ts">
import { formatId, formatMoney, transformTimestamp } from '@/utils/format';
import { getConsume } from '@/utils/request/payCard';
import { ref } from 'vue';
import { useRoute } from 'vue-router';
const route = useRoute();
const consumeId = route.params.consumeId + "";
const title = ref("");
const actualPrice = ref("");
const originPrice = ref("");
const discountPrice = ref("");
const gift = ref();
const discount = ref();
const sub = ref();
const consumeIdFomat = ref();
const payedTime = ref("");
const activeNames = ref([]);
getConsume(consumeId).then(response => {
    const consume = response.data.data;

    actualPrice.value = "￥" + formatMoney(consume.actualPrice);
    originPrice.value = "￥" + formatMoney(consume.payedAmount);
    discountPrice.value = "￥" + formatMoney(consume.discountPrice);
    const subNumber = consume.payedAmount - consume.actualPrice;
    if (subNumber > 0) {
        sub.value = "-￥" + formatMoney(subNumber);
    } else {
        sub.value = "￥" + formatMoney(subNumber);
    }
    const discountNumber = consume.payedAmount - consume.discountPrice;
    discount.value = (discountNumber > 0 ? "-￥" : "￥") + formatMoney(discountNumber);
    gift.value = (consume.gift > 0 ? "-￥" : "￥") + formatMoney(consume.gift);
    payedTime.value = transformTimestamp(consume.payedTime);
    consumeIdFomat.value = formatId(consumeId);
    if (consume.state != 0) {
        title.value = "支付失败";
        sub.value = "￥" + formatMoney(null);
        discount.value = "￥" + formatMoney(null);
        gift.value = "￥" + formatMoney(null);
    }else{
        title.value = "支付成功";
    }
})

// const response = await getConsume(consumeId);
// console.log(response.data);

</script>

<style scoped>
.title {
    text-align: center;
    min-height: 150px;
    line-height: 150px;
}
</style>