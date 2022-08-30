<template>
    <div class="cards">
        <Card :title="card.cardName" @click="onClick" v-if="card"
            :disable="cardStatus != 0 || (expireTime && new Date(expireTime) < new Date())"
            :value="cardRefunded ? '已退卡' : cardRefunding ? '退卡中' : cardExpired ? '已过期' : ''" />
    </div>
</template>
<script lang="ts">
import { getCardById } from '@/utils/request/card';
import { defineComponent, ref } from 'vue';
import { useRouter } from 'vue-router';

export default defineComponent({
    props: {
        walletId: String,
        id: { type: [String, Number], required: true },
        cardId: { type: [String, Number], required: true },
        merchantId: { type: [String, Number] },
        cardStatus: { type: [String, Number] },
        instanceId: { type: [String, Number] },
        expireTime: { type: [String, Number, Date] },
    },
    setup(props) {
        const router = useRouter();
        const card = ref();
        //预付卡状态
        const cardRefunded = ref(false);
        const cardRefunding = ref(false);
        const cardExpired = ref(false);
        getCardById(String(props.cardId)).then(response => {
            if (response.data.code === 0 && response.data.data) {
                card.value = response.data.data;
            } else {

            }
        }).catch(error => { });
        const now = new Date();
        const expire = new Date(props.expireTime+"");
        if (props.cardStatus == 2) {
            cardRefunded.value = true;
        }
        else if (props.cardStatus == 1) {
            cardRefunding.value = true;
        }
        if (expire < now) {
            cardExpired.value = true;
        }

        const onClick = () => {
            console.log("click" + props.id);

            router.push("/mycard/" + props.cardId + "/" + props.id);

        }
        return {
            card, cardRefunded, cardRefunding, cardExpired,
            onClick,
        }
    },
    components: {

    },
})
</script>
<style scoped>
.cards {
    margin-top: 15px;
}
</style>
