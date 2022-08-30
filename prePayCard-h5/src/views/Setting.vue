<template>
    <!-- <div class="nav">
        <Navigation title="设置" />
        
    </div> -->
    <div class="list">
		<van-cell-group title=" ">
			<van-cell title="余额" is-link to="/balance" />
			<van-cell title="账单" is-link to="/record" />
		</van-cell-group>
		<van-cell-group title=" ">
			<van-cell title="退卡记录" is-link to="/refund"/>
			<van-cell title="扫一扫" is-link to="/scan"/>
		</van-cell-group>
		<van-cell-group title="账号管理" v-if="store.state.user">
			<van-cell to="/login">
				<template #title>
					<div class="cellButton">
						切换账号
					</div>
				</template>
			</van-cell>
		</van-cell-group>
		<van-cell-group>
			<van-cell @click="logout"  v-if="store.state.user">
				<template #title>
					<div class="cellButton">
						退出登录
					</div>
				</template>
			</van-cell>
		</van-cell-group>
	</div>
</template>

<script lang="ts">
import store from '@/store';
import { Toast } from 'vant';
import { defineComponent } from 'vue'
import Navigation from '@/components/Navigation.vue';
import { useRouter } from 'vue-router';
export default defineComponent({
    setup() {
        const router = useRouter();
        const logout = () => {
            store.commit("setToken", "");
            store.commit("setUser", null);
            Toast.success("已退出当前账号");
            window.location.reload();
            router.push("/");
        };
        return { logout,store };
    },
    components: { Navigation }
})
</script>

<style scoped>
.cellButton {
    text-align: center;
}
</style>