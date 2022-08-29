
// zzk add for api pages test
import { request, METHOD} from '@/utils/request'

export async function api_get_test(url) {
    return request(url, METHOD.GET)
}
export default {
    api_get_test
}
