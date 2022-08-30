<template>
    <div class="logged">
        <van-row gutter="10">
            <van-col span="6">
                <van-image round fit="cover" class="avatar" height="2rem" width="2rem"
                    src="https://fastly.jsdelivr.net/npm/@vant/assets@1.0.6/logo.png" />
            </van-col>
            <van-col span="16">
                <van-row>
                    <div class="userName">{{ username }}</div>
                </van-row>
                <van-row>
                    <div class="userInfo">{{ info }}</div>
                </van-row>
                <van-row>
                    <div class="userInfo">上次登录:{{ last }}</div>
                </van-row>
            </van-col>
        </van-row>
    </div>
</template>
<script lang="ts">
import { defineComponent } from "vue"

export default defineComponent({
    props: {
        username: String,
        info: String,
        last:String,
    },
    setup(props) {
        props.username,
            props.info
    },
    components: {

    },
})
</script>
<style>
.logged .userName {
    font-size: 28px;
    font-weight: bold;
    height: 1rem;
    line-height: 1rem;
}

.logged .userInfo {
    font-size: 14px;
    height: 0.5rem;
    line-height: 0.5rem;
    font-weight:lighter;
}
</style>
