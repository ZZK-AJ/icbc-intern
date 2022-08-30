<template>
	<div class="nav">
		<navigation title="卡包" :noArrow="true" />
	</div>
	<Unauthorized v-if="notLogged" />

	<div class="main" v-if="!notLogged">
		<van-pull-refresh v-model="loading" @refresh="onRefresh" class="container">
			<van-tabs v-model:active="active" swipeable>
				<van-tab :title="'可使用(' + (payCards ? payCards.length : 0) + ')'">
					<div v-for="payCard in payCards" class="miniCard">

						<MyCardPayCard v-bind="payCard" />
					</div>
					<div class="empty">
						<van-empty v-if="payCards && payCards.length == 0" description="还没有预付卡" />
					</div>
				</van-tab>
				<van-tab :title="'不可用(' + (invalidPayCards ? invalidPayCards.length : 0) + ')'">
					<div v-for="payCard in invalidPayCards" class="miniCard">

						<MyCardPayCard v-bind="payCard" />
					</div>
					<div class="empty">
						<van-empty v-if="invalidPayCards && invalidPayCards.length == 0" description="还没有预付卡" />
					</div>
				</van-tab>
			</van-tabs>



		</van-pull-refresh>

	</div>

</template>
<script lang="ts" setup>
import { ref } from 'vue';
import { getPayCardByUserId } from '@/utils/request/payCard'
import Unauthorized from '@/views/401.vue'
import { useStore } from 'vuex';

const payCards: any = ref(null);
const invalidPayCards: any = ref(null);
const store = useStore();
const user = store.state.user;
const token = store.state.token;

const notLogged = ref(false);
const loading = ref(false);

const active = ref(0);

const getData = () => {
	if (user == null || token === "") {
		notLogged.value = true;
		store.commit("setUser", null);
		store.commit("setToken", "");
	} else if (payCards.value == null) {
		//请求
		getPayCardByUserId(user.userId).then(response => {
			if (response.status === 401) {
				notLogged.value = true;
				store.commit("setUser", null);
				store.commit("setToken", "");
			}
			if (response.data.code === 0) {
				payCards.value = response.data.data.filter((item: any) => item.cardStatus == 0 && new Date(item.expireTime) > new Date);
				invalidPayCards.value = response.data.data.filter((item: any) => item.cardStatus != 0 || new Date(item.expireTime) < new Date);
			}
		}).catch(error => {
			console.log(error)

		}).finally(() => {
			loading.value = false;
		});
	}
}
const onRefresh = () => {
	payCards.value = null
	getData();
}
getData();


</script>
<style scoped>
.main {
	margin-top: 10px;
}
.container{
	min-height: 80vh;
}
</style>
