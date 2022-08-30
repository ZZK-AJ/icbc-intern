<template>
    <div>
        <Navigation title="我的钱包" :fixed="true" />
        <div class="money">
            <div class="container">
                <van-row>
                    <van-col span="12" offset="6">
                        <van-image :src="img" width="1.7rem" height="1.7rem" />
                    </van-col>
                </van-row>
                <van-row>
                    <van-col span="12" offset="6">
                        <div class="my">我的零钱</div>
                    </van-col>
                    <van-col span="6"></van-col>
                </van-row>
                <van-row>
                    <van-col span="6">
                        <div class="icon">￥</div>
                    </van-col>
                    <van-col span="12">
                        <div class="number">
                            {{ balance }}
                        </div>
                    </van-col>
                    <van-col span="6"></van-col>
                </van-row>

            </div>

        </div>

    </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import Navigation from '@/components/Navigation.vue';
import { getBalanceByUser } from '@/utils/request/user';
import { formatMoney } from '@/utils/format';
import img from '@/assets/icon_rmb.jpg';
export default defineComponent({
    setup() {
        const balance = ref("0.00");

        getBalanceByUser().then(response => {
            if (response.data.code === 0)
                balance.value = formatMoney(response.data.data);
        })
        return {
            img, balance
        };
    },
    components: { Navigation }
})
</script>

<style scoped>
.money {
    min-height: 50vh;
    display: flex;
    place-items: center;
}

.money .container {
    min-width: 300px;
    text-align: center;
    margin: 0 auto;
}


.my {
    font-size: 16px;
    margin-top: 30px;
}

.icon {
    float: right;
    font-size: 28px;
    line-height: 50px;
}

.number {
    line-height: 50px;
    font-size: 40px;
    font-weight: bold;
}
</style>