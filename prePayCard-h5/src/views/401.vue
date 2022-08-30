<template>
	<van-empty description="登陆后查看">
		<van-button round type="danger" class="bottom-button" @click="onClick" :replace="true">立即登录</van-button>
	</van-empty>

</template>
<script lang="ts">
import { useRouter } from 'vue-router';

export default {
	setup() {
		const router = useRouter();
		const onClick = ()=>{
			router.replace("/login")
		}
		return {
			onClick,
		}
	},
	components: {

	},
}
</script>
<style>
</style>
