<template>
	<Navigation title="购买预付卡" />
	<van-empty v-if="notFound" image="network" description="当前页面不存在">
		<van-button round type="danger" class="bottom-button" @click="back">返回上一页</van-button>
	</van-empty>
	<div v-if="card" class="buy">

		<div class="card">
			<Card :title="card.cardName" :label="'面额:' + money"></Card>

		</div>
		<div class="van-doc-demo-block__title">
			预付卡简介
		</div>
		<div class="info">
			{{ card.cardInfo }}
		</div>
		<div class="van-doc-demo-block__title">
			预付卡说明
		</div>
		<div class="info">
			<van-row>
				<van-col span="6">
					<p>有效期：</p>
				</van-col>
				<van-col span="18">
					<p>{{card.expireDate}}年</p>
				</van-col>
			</van-row>
			<van-row>
				<van-col span="6">
					<p>折扣:</p>
				</van-col>
				<van-col span="18">
					<p v-if="card.discountRate<100">{{card.discountRate/10}}折</p>
					<p v-if="card.discountRate===100">无折扣</p>
				</van-col>
			</van-row>
			<van-row>
				<van-col span="6">
					<p>赠送金额:</p>
				</van-col>
				<van-col span="18">
					<p v-if="card.giftAmount>0">￥{{giftAmount}}</p>
					<p v-if="card.giftAmount===0">无赠送</p>
				</van-col>
			</van-row>
			<van-row>
				<van-col span="6">
					<p>购卡须知:</p>
				</van-col>
				<van-col span="18">
					<p>预付卡购买后仅限在有效期内消费。</p>
					<p>赠送金额在支付时部分抵扣,抵扣金额不会超过实际支付金额</p>
					<p>赠送金额未使用完部分,在退卡后不予退还</p>
				</van-col>
			</van-row>
		</div>
		<div>
			<van-action-bar>
				<van-action-bar-icon icon="chat-o" text="客服" @click="onClickIcon" />
				<van-action-bar-icon icon="cart-o" text="购物车" @click="onClickIcon" />
				<van-action-bar-icon icon="shop-o" text="店铺" @click="onClickIcon" />
				<van-action-bar-button type="danger" text="立即购买" @click="onClickButton" />
			</van-action-bar>
		</div>
	</div>
	<PayPassword v-model:show="showPassword" @finish="finishInput" />

</template>
<script lang="ts">
import card from 'vant/lib/card';
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import { buyCardById, getCardById } from '@/utils/request/card';
import Navigation from '../components/Navigation.vue';
import { Toast } from 'vant';
import Card from '@/components/MyCard/Card.vue';
import { formatMoney } from '@/utils/format';



const back = () => history.back();
const onClickIcon = () => Toast('暂不支持');

export default {
	setup() {
		const route = useRoute();
		let id = route.params.id as string;
		let notFound = ref(false);
		const card = ref();
		const money = ref("0.00");
		const giftAmount = ref("0.00");
		const showPassword = ref(false);
		const getData = async () => {
			const res = await getCardById(id);
			if (res.data.code === 0 && res.data.data) {
				card.value = res.data.data;
				money.value = formatMoney(card.value.cardAmount);
				giftAmount.value = formatMoney(card.value.giftAmount);
			} else {
				notFound.value = true;
			}
		};
		getData();

		const onClickButton = () => {
			showPassword.value = true;
		}
		const finishInput = () => {
			buyCardById(id).then(response => {
				if (response.data.code === 0) {
					Toast.success("购买成功");
				} else {
					setTimeout(() => {
						Toast.fail({
							message: response.data.msg,
							duration: 3000
						});
					}, 500)

				}
			}).catch(error => {
				console.log(error);

			})
		}
		return {
			card, notFound, money,giftAmount, showPassword,
			back, getData, onClickIcon, onClickButton, finishInput,
		}
	},
	components: {
		Navigation,
		Card
	},
}
</script>
<style scoped>
.buy {
	margin: 20px auto;
	width: 360px;
}

.buy .title {
	padding-left: 16px;
	font-size: large;
}

.buy .info {
	padding-left: 16px;
	font-size: medium;
}

.info {
	width: 350px;
	margin: 0 auto;
}

.info p {
	color: gray;
	font-size: medium;
	margin: 0;
	padding: 0;
}
</style>
