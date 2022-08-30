import { Md5 } from "ts-md5";
export const password = (str:string)=>{
    //加密
    const hash1 = Md5.hashStr(str);
    //加盐加密,盐值为密码本身
    const hash2 = Md5.hashStr(str+hash1);
    const hash3 = Md5.hashStr(str+hash2);
    return hash2;
}

export const hash = function(str:string) {
    var hash = 0, i, chr;
    if (str.length === 0) return hash;
    for (i = 0; i < str.length; i++) {
      chr   = str.charCodeAt(i);
      hash  = ((hash << 5) - hash) + chr;
      hash |= 0; // Convert to 32bit integer
    }
    return hash;
  };