<template>
    <van-nav-bar @click-left="onClickLeft" :fixed="fixed">
        <template #title>
            <span class="title" style=" font-weight: bold;">{{ title }}</span>
        </template>
        <template #left v-if="!noArrow">
            <van-icon name="arrow-left" size="0.48rem" color="#ee0a24" />
        </template>
    </van-nav-bar>
</template>
<script lang="ts">
import { defineComponent } from "vue"
import { useRouter } from "vue-router";

export default defineComponent({
    props: {
        title: String,
        noArrow: Boolean,
        fixed: { type: Boolean, default: false },
    },
    setup(props) {
        const router = useRouter();
        const onClickLeft = () => {
            if (document.referrer == '') {
                router.replace("/")
            } else {
                history.back();
            }
        }
        return {
            onClickLeft
        };
    },
    components: {

    },

})
</script>
<style scoped>
.title {
    font-weight: bold;
    font-size: 17px;
}
</style>
