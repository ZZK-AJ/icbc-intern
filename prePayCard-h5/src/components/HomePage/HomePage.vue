<template>
	<div class="nav">
		<navigation title="预付卡" :noArrow="true" />
	</div>
	<div class="search">
		<form action="/">
			<van-search showAction v-model="search" placeholder="请输入搜索关键词" @search="onSearch" @cancel="onCancel" background="#f7f8fa"/>
		</form>
	</div>
	<div class="cards">
		<div v-for="card in cards">
			<van-card :price="(card.cardAmount / 100.00).toFixed(2)" :desc="card.cardInfo" :title="card.cardName"
				:thumb="imgs[card.cardType]?imgs[card.cardType]:img_default" @click="onClick(card.id)">
				<!-- <template #footer>
					<van-button type="danger"  size="mini" round >购买</van-button>
				</template> -->
			</van-card>
		</div>
		<!-- 没有查询到预付卡显示 -->
		<div v-if="cards.length == 0">
			<van-empty image="search" description="没有查询到预付卡" />
		</div>
	</div>


</template>
<script lang="ts">
import search from "vant/lib/search";
import { ref } from "vue";
import { getAllCard } from "@/utils/request/card";
import Navigation from "../Navigation.vue";
import img_swim from '@/assets/游泳.jpg';
import img_gym from '@/assets/健身.jpg'
import img_cosmetic from '@/assets/美容.jpg'
import img_default from '@/assets/default.png'
import { useRouter } from "vue-router";

const imgs = {
	"游泳":img_swim,
	"健身":img_gym,
	"美容":img_cosmetic,
}
let res:any = null;
export default {


	setup() {
		const cards = ref();
		getAllCard().then((response)=>{
			cards.value = response.data.data;
			res = response;
		});
		
		const search = ref('');
		const router = useRouter();
		//执行搜索方法，通过filter过滤出结果
		const onSearch = (val: string) => {
			cards.value = res.data.data.filter((item: { cardName: string | string[]; cardInfo: string | string[]; }) => {
				if (item.cardName.indexOf(val) >= 0 || item.cardInfo.indexOf(val) >= 0) return true;
				return false;
			})
		}
		const onCancel = () => cards.value = res.data.data;

		const onClick = (cardId:number) =>{
			router.push('/card/' + cardId)
		}
		return {
			cards, search, res,
			imgs,img_default,
			onSearch, onCancel,onClick,
		}
	},
	components: {
		Navigation
	},
}
</script>
<style scoped>
.cards {
	margin-bottom: 60px;
}
</style>
