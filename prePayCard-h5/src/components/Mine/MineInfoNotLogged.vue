<template>
    <div class="not">
        <div class="notLogged">
            <van-image width="2rem" height="2rem" round fit="cover" class="avatar"
                :src="imgUrl" />
            <van-button round type="primary" color="#ee0a24" block @click="login">立即登录</van-button>
            <div class="tips">登陆后可查看您的资产情况</div>
        </div>

    </div>

</template>
<script lang="ts">


import imgUrl from '@/assets/logo_gray.png'
import { useRouter } from 'vue-router';
export default {
    setup() {
        const router = useRouter();
        const login = () => router.push("/login");
        return {
            login,
            imgUrl,
        }
    },
    components: {

    },
}
</script>
<style>
.not {
    display: flex;
    place-items: center;
    min-height: 90px;
}

.notLogged {
    text-align: center;
    margin: 0 auto;
    min-width: 200px;
}

.tips {
    font-size: small;
    color: #969799;
}
</style>
