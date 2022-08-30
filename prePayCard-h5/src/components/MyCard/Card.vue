<template>
    <div class="card">
        <van-config-provider :theme-vars="cardTheme">
            <van-cell-group inset>
                <van-cell>
                    <template #title>
                        <div class="container">
                            <div class="van-ellipsis">
                                {{ title }}
                            </div>
                        </div>


                    </template>
                    <template #label>
                        <div class="label">
                            <span>{{ label }}</span>
                        </div>
                    </template>

                    <template #value>
                        <div class="value">
                            {{ value }}
                        </div>
                        
                    </template>
                </van-cell>
            </van-cell-group>
        </van-config-provider>

    </div>
</template>
<script lang="ts">
import { hash } from '@/utils/md5';
import { defineComponent, ref } from 'vue';

const colors = [
    //红色
    // "linear-gradient(to right, #ff6034, #ee0a24)",
    // //橙色
    // "linear-gradient(-20deg, #fc6076 0%, #ff9a44 100%)",
    // //绿色
    // // "linear-gradient(to top, #37ecba 0%, #72afd3 100%)",
    // //蓝色
    // "linear-gradient(to top, #00c6fb 0%, #005bea 100%)",
    //紫色
    // "linear-gradient(to top, #9795f0 0%, #fbc8d4 100%)",
    //白色
    // "linear-gradient(to top, #cfd9df 0%, #e2ebf0 100%)",

    "linear-gradient(to top, #5ee7df 0%, #b490ca 100%)",

    "linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%)",

    "linear-gradient(to top, #9890e3 0%, #b1f4cf 100%)",

];


export default defineComponent({
    props: {
        title: String,
        label: String,
        value: String,
        disable: { type: Boolean, default: false },
    },
    setup(props) {
        //自定义样式
        const cardTheme = {
            cellBackgroundColor: "#c8c9cc",
            cellTextColor: "#fff",
            cellLabelColor: "#fff",
            cellValueColor: "#fff"
        };

        const hashCode = hash(props.title + "");
        if (!props.disable) {
            cardTheme.cellBackgroundColor = colors[hashCode % colors.length]
        }

        return {
            cardTheme,
        }
    },
    components: {

    },
})
</script>
<style scoped>
.card van-cell-group {
    background-color: gray;

}

.container {
    width: 200px;
    font-size: 20px;
    font-weight: bold;
}

.label {
    min-height: 100px;
    font-size: 18px;
    line-height: 50px;
}
.value{
    font-size: 18px;
}
</style>
