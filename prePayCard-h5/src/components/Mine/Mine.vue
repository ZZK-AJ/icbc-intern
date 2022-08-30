<template>
	<MineInfo />
	<Setting />

	<!-- <van-grid :column-num="4">
		<van-grid-item icon="gold-coin-o" text="余额" to="/balance" />
		<van-grid-item icon="photo-o" text="文字" />
		<van-grid-item icon="balance-list-o" text="账单" to="/record" />
		<van-grid-item icon="setting-o" text="设置" to="/setting" />
	</van-grid> -->


</template>

<script lang="ts">
import { Toast } from "vant";
import Setting from "@/views/Setting.vue";
function setting() {
	Toast.success("打开设置");
}
export default {
	setup() {
		return {
			setting,
		}
	},
	components: {
    Setting
},
}
</script>

<style scoped>
.avator {
	margin-right: 0px;
	border: 2px black;
}
</style>
