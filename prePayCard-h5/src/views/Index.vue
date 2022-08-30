<template>
	<KeepAlive>
		<component :is="currentComponents[active]"></component>
	</KeepAlive>
	<van-tabbar v-model="active" active-color="#ee0a24" inactive-color="#000" @change="onChange">
		<van-tabbar-item icon="home-o">首页</van-tabbar-item>
		<van-tabbar-item icon="paid">卡包</van-tabbar-item>
		<van-tabbar-item icon="user-o">我的</van-tabbar-item>
	</van-tabbar>
</template>
<script lang="ts">
import { Dialog } from 'vant';
import { defineAsyncComponent, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useStore } from 'vuex';
const MyCard = defineAsyncComponent({
	// 加载函数
	loader: () => import('@/components/MyCard/MyCard.vue'),

	// 加载异步组件时使用的组件
	loadingComponent: import('@/components/Loading.vue'),
	// 展示加载组件前的延迟时间，默认为 200ms
	delay: 200,

	// 加载失败后展示的组件
	errorComponent: import('../views/404.vue'),
	// 如果提供了一个 timeout 时间限制，并超时了
	// 也会显示这里配置的报错组件，默认值是：Infinity
	timeout: 3000
})

const Home = defineAsyncComponent({
	loader: () => import('@/components/HomePage/HomePage.vue'),
	loadingComponent: import('@/components/Loading.vue'),
	delay: 200,
	errorComponent: import('../views/404.vue'),
	timeout: 3000
})
const Mine = defineAsyncComponent({
	loader: () => import('@/components/Mine/Mine.vue'),
	loadingComponent: import('@/components/Loading.vue'),
	delay: 200,
	errorComponent: import('../views/404.vue'),
	timeout: 3000
})
let ua = navigator.userAgent.toLowerCase();
const match = ua.match(/MicroMessenger/i);
let isWexin = false;
if (match === null) {
	console.log("其他环境中（例如浏览器等等）");
} else if (match.includes("micromessenger")) {
	console.log("微信环境中");
	// Dialog.alert({title:"微信浏览可能出现问题，请在浏览器中打开"});
}
const active = ref(0);
export default {

	setup() {
		const route = useRoute();
		const store = useStore();
		let currentComponents = [Home, MyCard, Mine];
		const index = route.query.index;
		// if(index){
		// 	store.commit("setCurrentIndex", index);
		// }
		active.value = store.state.currentIndex
		const onChange = (index: number) => {
			store.commit("setCurrentIndex", index);
		}
		return {
			active,
			currentComponents,
			onChange
		};
	},
	activated() {
		const route = useRoute();
		const router = useRouter();
		const store = useStore();
		const index = route.query.index;
		if (index) {
			store.commit("setCurrentIndex", index);
			router.replace("/");
			console.log(active.value);
		}
		active.value = store.state.currentIndex;
	},
	components: {

	},


};
</script>
<style>
</style>
