import api from "./axios";
//共用解包function
const unwrap = (res) => res.data.data
//建立一個URL
const BASE_URL = 'api/admin/card-txns'
//查全部交易
export const getTransactions = (page=0, size=10) => {
    return api.get(BASE_URL,{
        params:{
            page,
            size
        },
    }).then(unwrap);

};
//後台刷退
export const refundTransaction=(id)=>api.post(`${BASE_URL}/${id}/refund`).then(unwrap);

