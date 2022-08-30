<template>
    <div class="mineInfo">
        <component :is="componentId" v-bind="props" />
    </div>

</template>
<script lang="ts">
import { formatId } from "@/utils/format";
import { ref } from "vue";
import { useStore } from "vuex";
import MineInfoLoggedVue from "./MineInfoLogged.vue";
import MineInfoNotLoggedVue from "./MineInfoNotLogged.vue";

const componentId = ref("MineInfoNotLoggedVue");
const props = ref();
export default {

    setup() {
        const store = useStore();
        const token = store.state.token;
        const user = store.state.user;


        if (user != null) {
            componentId.value = "MineInfoLoggedVue";
            const username = user.userName;
            props.value = { info: "个人信息", username: username, last: user.last }
        }



        return {
            componentId,
            props,
        }
    },
    activated() {
        const store = useStore();
        const token = store.state.token;
        const user = store.state.user;
        console.log(user);


        if (user != null) {
            componentId.value = "MineInfoLoggedVue";
            const username = user.userName;
            props.value = { info: "个人信息", username: username, last: user.last }
        }
    },
    components: {
        MineInfoLoggedVue,
        MineInfoNotLoggedVue,
    },
}
</script>
<style>
.mineInfo {
    margin: 30px auto;
    width: 335px;
    min-height: 60px;
}
</style>
