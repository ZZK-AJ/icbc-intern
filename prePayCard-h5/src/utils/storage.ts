export const getItem = (key: string) => {
    let data = window.localStorage.getItem(key)
    if (data == null) {
        return null;
    }
    try {
        return JSON.parse(data);
    } catch {
        return data;
    }
}

export const setItem = (key: string, value: any) => {
    if (typeof value === 'object') {
        value = JSON.stringify(value);
    }
    window.localStorage.setItem(key, value);
}


export const removeItem = (key: string) => {
    window.localStorage.removeItem(key);
}