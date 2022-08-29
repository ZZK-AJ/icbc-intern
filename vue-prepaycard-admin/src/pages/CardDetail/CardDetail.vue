<template>
  <div>
    <h2> 预付卡销售总览页 </h2>

<!--    <iframe src="http://localhost:3000/d/E49YBvW4k/shang-hu-yu-fu-qia-xiao-shou-shu-ju-copy?orgId=1&kiosk=tv" width="1150" height="270" frameborder="0"></iframe>-->

    <a-table @expand="expand" :columns="columns" :data-source="data" :loading="loading"
             @change="handleTableChange" :expandIconColumnIndex="8" :expandIconAsCell="false" >
<!--      slot-scope="text"  slot-scope="record"  -->
      <a-table slot="expandedRowRender"
               :columns="innerColumns" :data-source="innerData" :pagination="false">
      </a-table>
    </a-table>
  </div>

</template>
<script>
import {getMerchantPayCardInfo, getPayedCardConsumeInfo} from '@/services/user'
import {mapGetters} from 'vuex'  // 状态管理，mapMutations 设置状态，mapGetters 获取状态

const columns = [
  {
    title: '购买用户名',
    dataIndex: 'userName',
    sorter: true,
    width: '10%',
    scopedSlots: { customRender: 'userName' },
  },
  {
    title: '预付卡状态',
    dataIndex: 'cardStatus',
    width: '10%',
  },
  {
    title: '预付卡名称',
    dataIndex: 'cardName',
    sorter: true,
    width: '10%',
    scopedSlots: { customRender: 'cardName' },
  },
  {
    title: '预付卡类型',
    dataIndex: 'cardType',
    width: '10%',
  },
  {
    title: '预付卡金额/元',
    dataIndex: 'cardAmount',
    width: '10%',
  },
  {
    title: '赠送金额/元',
    dataIndex: 'giftAmount',
    width: '10%',
  },
  {
    title: '消费折扣/%',
    dataIndex: 'discountRate',
    width: '10%',
  },
  {
    title: '过期日期',
    dataIndex: 'expireDate',
    width: '10%',
    key: "expireDate",
  },
  {
    title: '消费详情',
    dataIndex: '',
    width: '10%',
  },
];

const innerColumns = [
  { title: '支付金额', dataIndex: 'payedAmount', key: 'payed_amount' },
  { title: '支付时间', dataIndex: 'payedTime', key: 'payed_time' },
  { title: '折扣后金额', dataIndex: 'discountPrice', key: 'discount_price' },
  { title: '抵扣金额', dataIndex: 'gift', key: 'gift' },
  { title: '实际支付金额', dataIndex: 'actualPrice', key: 'actual_price' },
];

export default {
  data() {
    return {
      data: [],
      pagination: {},
      loading: false,
      columns,
      innerColumns,
      innerData: [],
      openRowKeys:"[]",
    };
  },
  computed: {
    ...mapGetters("merchant_zt", ["merchantID"]),  // 在计算属性中，对 mapGetters 进行初始化
    ...mapGetters("account", ["user"]),
    systemName () {
      return this.$store.state.setting.systemName
    }
  },
  mounted() {
    this.fetch();
  },
  methods: {
    handleTableChange(pagination, filters, sorter) {
      console.log(pagination);
      const pager = { ...this.pagination };
      pager.current = pagination.current;
      this.pagination = pager;
      this.fetch({
        results: pagination.pageSize,
        page: pagination.current,
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...filters,
      });
    },

    fetch() {
      this.loading = true;
      // 获取外层表格的数据
      getMerchantPayCardInfo(this.merchantID).then(({ data }) => {
        console.log(data)

        if (data.code === 0) {
          console.log(data.data)
          // card_status '预付卡状态，正常 0, 不可用 1（代表用户已提交退卡申请）, 已退卡 2'
          for (let i in data.data) {
            let payedCard = data.data[i]
            console.log(payedCard)

            if (payedCard.cardStatus === 0) {
              payedCard.cardStatus = "正常"
            } else if (payedCard.cardStatus === 1) {
              payedCard.cardStatus = "退卡中"
            } else {
              payedCard.cardStatus = "已退卡"
            }
            // todo payedCard 表里面也要添加这个内容 现在默认都被赋值为 3 年了
            if (payedCard.expireDate === "1") {
              payedCard.expireDate = "1年"
            } else if (payedCard.expireDate === "2") {
              payedCard.expireDate = "2年"
            } else {
              payedCard.expireDate = "3年"
            }

            // 转换显示的预付卡价格
            if (payedCard.cardAmount === null || payedCard.cardAmount === undefined) {
              payedCard.cardAmount = "0.00"
            } else {
              payedCard.cardAmount = (payedCard.cardAmount / 100.00).toFixed(2)
            }
            // 转换显示的赠送金额
            if (payedCard.giftAmount === null || payedCard.giftAmount === undefined) {
              payedCard.giftAmount = "0.00"
            } else {
              payedCard.giftAmount = (payedCard.giftAmount / 100.00).toFixed(2)
            }
            console.log(payedCard)

          }

          const pagination = { ...this.pagination };
          // Read total count from server
          // pagination.total = data.totalCount;
          pagination.total = 200;
          this.loading = false;
          this.pagination = pagination;

          this.data = data.data;
        } else {
          // 错误代码为 1
          this.error = data.msg
          this.loading = false
          this.$message.error(data.msg)
        }

      }).catch(error => {
        console.log(error, "error")
        this.loading = false
      }); // 失败的返回
    },

    expand(expanded, record) {
        // todo 解决数据重复的问题
        getPayedCardConsumeInfo(record.cardId).then(({ data }) => {
          let innerData = []
          console.log(data.data)
          for (let i in data.data) {
            let consume = data.data[i]
            // 转换显示的 discountPrice
            if (consume.discountPrice === null || consume.discountPrice === undefined) {
              consume.discountPrice = "0.00"
            } else {
              consume.discountPrice = (consume.discountPrice / 100.00).toFixed(2)
            }
            // 转换显示的 payedAmount
            if (consume.payedAmount === null || consume.payedAmount === undefined) {
              consume.payedAmount = "0.00"
            } else {
              consume.payedAmount = (consume.payedAmount / 100.00).toFixed(2)
            }
            // 转换显示的 actualPrice
            if (consume.actualPrice === null || consume.actualPrice === undefined) {
              consume.actualPrice = "0.00"
            } else {
              consume.actualPrice = (consume.actualPrice / 100.00).toFixed(2)
            }
            // 转换显示的 gift
            if (consume.gift === null || consume.gift === undefined) {
              consume.gift = "0.00"
            } else {
              consume.gift = (consume.gift / 100.00).toFixed(2)
            }
            console.log(consume)
            innerData.push(consume)
          }
          this.innerData = innerData
        }).catch(error => {
          console.log(error, "error")
          this.loading = false
        });
    }
  },
};
</script>
