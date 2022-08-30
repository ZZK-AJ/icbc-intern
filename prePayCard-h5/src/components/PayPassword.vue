<template>


	<van-overlay :show="show" @click="">
		<div class="passwordContainer">
			<div class="password">
				<van-row>
					<van-col span="6">
						<div class="cancel"  @click="cancel">
							取消
						</div>
					</van-col>
				</van-row>
				<van-row>
					<van-col span="6">
					</van-col>
					<van-col span="12">
						<div class="title">
							请输入支付密码
						</div>
					</van-col>
				</van-row>
				<div class="input">
					<van-password-input :value="payPasswd" />
				</div>
				<!-- 数字键盘 -->
				<van-number-keyboard v-model="payPasswd" :show="true" />
			</div>
		</div>

	</van-overlay>


</template>


<script lang="ts" setup>
//因emit需要,这里写setup语法糖
import { defineComponent, defineEmits, ref, watch } from 'vue';
const payPasswd = ref("");
const props = defineProps({
	show: Boolean,
})

const emit = defineEmits(['update:show', 'finish']);

const finishInput = () => {
	emit('update:show', false);
	emit('finish', payPasswd);
}
const cancel =()=>{
	emit('update:show', false);
}
watch(payPasswd, (newVal) => {
	if (newVal.length === 6) {
		finishInput();
		payPasswd.value = "";

	} else {

	}
});
</script>
<style scoped>
.passwordContainer {
	min-height: 50vh;
	display: flex;
	place-items: center;
}

.password {
	width: 300px;
	min-height: 250px;
	border-radius: 10px;
	background-color: white;
	margin: 0 auto;
}

.password .cancel {
	font-size: medium;
	color: gray;
	margin-top: 10px;
	margin-left: 20px;
}



.password .title {
	text-align: center;
	margin-top: 10px;
	font-size: large;
	/* font-weight: bold; */
}

.password .input {
	margin-top: 30px;
}
</style>
