<template>
    <div class="background">
        <video id="video" src="" class="camera"></video>
        <canvas class="camera" id="canvas" hidden></canvas>
        <!-- <van-button @click="start">start</van-button> -->
    </div>
</template>

<script lang="ts" setup>

import { useRoute, useRouter } from 'vue-router';
import qrcodeParser from 'qrcode-parser'
import { onDeactivated, onMounted } from 'vue';
import { Dialog } from 'vant';
const router = useRouter();
const route = useRoute();
let camera: any;


const start = () => {
    console.log("start");
    let video: any = document.getElementById('video');
    navigator.mediaDevices.getUserMedia({
        video: {
            facingMode: 'environment'
        }
    }).then(function (stream: any) {
        console.log(stream);
        try {
            camera = stream;
            video.src = window.URL.createObjectURL(stream);
            console.log("old");

        } catch {
            video.srcObject = stream;
            console.log("new");
        }

        video.play();
        setTimeout(check, 1000);
    })
};
const check = () => {
    let video: any = document.getElementById('video');
    let canvas: any = document.getElementById('canvas');
    canvas.height = video.clientHeight;
    canvas.width = video.clientWidth;
    // console.log(canvas.height + " " + canvas.width);

    const ctx = canvas.getContext('2d');
    ctx.drawImage(video, 0, 0, canvas.width, canvas.height)
    const imgsrc = canvas.toDataURL('image/png');

    qrcodeParser(imgsrc).then(res => {
        try {
            interface QrcodeJson {
                type: string,
                merchantId: number,
            }
            const result: QrcodeJson = JSON.parse(res);
            if (result.type === "merchantCode") {
                let payCardId = route.query.from ? route.query.from : 0;
                router.push("/mycard/0/" + payCardId + "/pay?merchantId=" + result.merchantId);
            } else {
                throw new Error('暂不支持的二维码');
            }

        } catch {
            Dialog.alert({ title: "暂不支持的二维码" }).then(() => {
                router.back();
            })
        } finally {
            camera.getTracks().map((item: any) => { item.stop() });
        }


    }).catch(error => {
        setTimeout(check, 500);
    })

};
onMounted(() => {
    start();
    window.addEventListener("popstate", function (e) {
      camera.getTracks().map((item: any) => { item.stop() });
    });
});
onDeactivated(()=>{
    
})
</script>

<style scoped>
.background{
    background-color: black;
    height: 100vh;
    display: flex;
    place-items: center;
}
.camera {
    width: 375px;
    /* min-height: 100vh; */
}
</style>