<template>
    <div class="app">
        <navigation title="登录" />

        <van-form @submit="onSubmit" class="main">
            <van-cell-group inset>
                <van-field v-model="username" name="userName" label="用户名" placeholder="用户名"
                    :rules="[{ required: true, message: '请填写用户名' }]" />
                <van-field v-model="password" type="password" name="loginPasswd" label="密码" placeholder="密码"
                    :rules="[{ required: true, message: '请填写密码' }]" />
            </van-cell-group>
            <div style="margin: 16px;">
                <van-button round block type="primary" color="#ee0a24" native-type="submit">
                    登录
                </van-button>
            </div>
            <van-cell-group inset class="link">
                <div>
                    没有账号?<a href="/register">立即注册</a>
                </div>
            </van-cell-group>

        </van-form>
    </div>



</template>
<script lang="ts">
import { login } from "@/utils/request/user";
import { ref } from 'vue';
import { Toast } from "vant";
import { useStore } from 'vuex'
import Navigation from "@/components/Navigation.vue";
import router from "@/router";
import { transformTimestamp } from "@/utils/format";


export default {
    setup() {
        const username = ref("");
        const store = useStore();
        const password = ref("");
        console.log(history);

        const onSubmit = async (values: any) => {
            const res = await login(values);
            if (res.data.code === 0) {
                Toast.success("登陆成功");
                const { token, userId } = res.data.data;
                store.commit("setToken", token);
                store.commit("setUser", { "userName": username.value, "userId": userId, last: transformTimestamp(new Date()) });
                if (document.referrer == '') {
                    router.replace("/")
                } else {
                    history.back();
                }

                // router.replace("/");
            }
            else {
                Toast.fail(res.data.msg);
            }
        };
        const onClickLeft = () => history.back();
        return {
            username,
            password,
            onSubmit,
            onClickLeft,
        };
    },
    components: { Navigation }
};

</script>
<style scoped>
.main {
    margin-top: 10px;
}

.title {
    font-weight: bold;
    font-size: 17px;
}

.link {
    float: right;
    font-size: 14px;
    background-color: #f7f8fa;
}

.link a {
    color: #1989fa;

}
</style>
