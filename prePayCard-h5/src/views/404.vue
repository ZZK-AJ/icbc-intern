<template>
	<van-empty image="network" description="当前页面不存在">
		<van-button round type="danger" class="bottom-button" @click="back">返回上一页</van-button>
	</van-empty>

</template>
<script lang="ts">
const back = () => history.back();
export default {
	setup() {
		return {
			back
		}
	},
	components: {

	},
}
</script>
<style>
</style>
