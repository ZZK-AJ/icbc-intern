<template>
	<div class="app">
		<navigation title="注册" />

		<!-- name属性与接口body对应,直接传递无需封装 -->
		<van-form @submit="onSubmit">
			<h2 class="van-doc-demo-block__title">用户名</h2>
			<van-cell-group inset>
				<van-field v-model="username" name="userName" label="用户名" placeholder="请输入用户名"
					:rules="[{ required: true, message: '用户名不能为空' }]" />
			</van-cell-group>

			<h2 class="van-doc-demo-block__title">登陆密码</h2>
			<van-cell-group inset>
				<van-field v-model="password" type="password" name="loginPasswd" label="密码" placeholder="请输入密码"
					:rules="[{ required: true, message: '密码不能为空' }]" />

				<van-field v-model="checkPassword" type="password" name="checkPassword" label="确认密码"
					placeholder="再次输入密码" :rules="[{ validator: checkPasswordValidator }]" />
			</van-cell-group>

			<h2 class="van-doc-demo-block__title">支付密码</h2>
			<van-cell-group inset>
				<van-field v-model="payPassword" type="password" name="payPasswd" label="密码" placeholder="请输入密码"
					:rules="[{ required: true, message: '密码不能为空' }]" />

				<van-field v-model="checkPayPassword" type="password" name="checkPayPassword" label="确认密码"
					placeholder="再次输入密码" :rules="[{ validator: checkPayPasswordValidator }]" />
			</van-cell-group>
			<div style="margin: 16px;">
				<van-button round block type="primary" color="#ee0a24" native-type="submit">
					立即注册
				</van-button>
			</div>
		</van-form>
	</div>



</template>
<script lang="ts">
import { register } from "@/utils/request/user";
import { ref } from 'vue';
import { Toast } from "vant";
import router from "../router";


export default {
	setup() {
		const username = ref('');
		const password = ref('');
		const checkPassword = ref('');
		const payPassword = ref('');
		const checkPayPassword = ref('');
		const showKeyboard = ref(true);
		const onSubmit = async (values: any) => {
			console.log(values);
			//过滤
			const { userName, loginPasswd, payPasswd } = values;
			const res = await register({ userName, loginPasswd, payPasswd });
			if (res.data.code === 0) {
				Toast.success("注册成功");
				router.replace("/login")
			} else {
				Toast.fail(res.data.msg);
			}

		};
		const checkPasswordValidator = (val: string) => {
			if (val === '') {
				return "请再次输入密码"
			}
			if (val === password.value) {
				return true;
			}
			return "两次输入密码不一致";
		};
		const checkPayPasswordValidator = (val: string) => {
			if (val === '') {
				return "请再次输入密码"
			}
			if (val === payPassword.value) {
				return true;
			}
			return "两次输入密码不一致";
		};



		return {
			username,
			password,
			checkPassword,
			payPassword,
			checkPayPassword,
			showKeyboard,
			onSubmit, checkPasswordValidator, checkPayPasswordValidator
		};
	},
};

</script>
<style>
</style>
