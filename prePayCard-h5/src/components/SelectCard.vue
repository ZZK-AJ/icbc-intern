<template>


	<van-overlay :show="show" @click="">
		<div class="Container">
			<div class="select">
				<van-picker title="请选择预付卡" item-height="1rem" :columns="options" @confirm="onConfirm" @cancel="onConfirm"/>
			</div>
		</div>

	</van-overlay>


</template>


<script lang="ts" setup>
//因emit需要,这里写setup语法糖
import { defineComponent, defineEmits, ref, watch } from 'vue';
const props = defineProps({
	show: Boolean,
	options: Array<String>,
})

const emit = defineEmits(['update:show', 'finish']);

const onConfirm = (value:string, index:number) => {
	emit('update:show', false);
	emit('finish', index);
}
const cancel = () => {
	emit('update:show', false);
}

</script>
<style scoped>
.Container {
	min-height: 100vh;
	display: flex;
	place-items: center;
}

.select {
	width: 300px;
	min-height: 150px;
	border-radius: 10px;
	background-color: white;
	margin: 0 auto;
	padding: 10px;
}

</style>
