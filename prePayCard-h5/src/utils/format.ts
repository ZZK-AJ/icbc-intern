export const transformTimestamp = (timestamp: string | number | Date) => {
    let a = new Date(timestamp).getTime();
    const date = new Date(a);
    const Y = date.getFullYear() + '-';
    const M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    const D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + '  ';
    const h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    const m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes());
    // const s = date.getSeconds(); // 秒
    const dateString = Y + M + D + h + m;
    // console.log('dateString', dateString); // > dateString 2021-07-06 14:23
    return dateString;
}

export const formatDateFromTimestamp = (timestamp: string | number | Date) => {
    let a = new Date(timestamp).getTime();
    const date = new Date(a);
    const Y = date.getFullYear() + '-';
    const M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    const D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + '  ';
    const h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    const m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes());
    // const s = date.getSeconds(); // 秒
    const dateString = Y + M + D;
    // console.log('dateString', dateString); // > dateString 2021-07-06 14:23
    return dateString;
}
export const formatMoney = (money:number|null|undefined)=>{
    if(money == null||money == undefined){
        return "0.00";
    }
    if (money < 100*1e3*100){
        return (money / 100.00).toFixed(2);
    }else if (money < 100*1e7*100){
        return (money / 100.00 /1e3).toFixed(2)+"万";
    }
    else if (money < 100*1e9*100){
        return (money / 100.00 /1e6).toFixed(2)+"M";
    }else{
        return (money / 100.00 /1e9).toFixed(2)+"B";
    }
}

export const formatId = (id:number|string)=>{
    let str:string = id+"";
    str = "0".repeat(16-str.length)+str;
    return str;
}