<template>
  <div>
    <h2> 预付卡退卡审核页 </h2>

    <a-table :columns="columns" :data-source="data" :loading="loading" @change="handleTableChange">

      <span slot="action" slot-scope="text, record">
        <a-popconfirm placement="topRight" v-if="data.length" title="是否确认通过该用户退卡审核" @confirm="() => refundPassAction(record)">
          <a-button >通过</a-button>
        </a-popconfirm>
        <a-divider type="vertical"/>
        <a-popconfirm placement="topRight" v-if="data.length" title="是否确认拒绝该用户退卡" @confirm="() => refundRejectAction(record)">
          <a-button>拒绝</a-button>
        </a-popconfirm>
      </span>
    </a-table>

  </div>

</template>
<script>
import {getRefundInfo, refundPass, refundReject} from '@/services/user'
import {mapGetters} from 'vuex'  // 状态管理，mapMutations 设置状态，mapGetters 获取状态

const columns = [
  {
    title: '用户名',
    dataIndex: 'userName',
    key: 'userName',
    sorter: true,
    scopedSlots: {customRender: 'userName'},
    width: '10%',
  },
  {
    title: '预付卡名称',
    dataIndex: 'cardName',
    sorter: true,
    width: '10%',
    scopedSlots: {customRender: 'cardName'},
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
  // {
  //   title: '赠送余额/元',
  //   dataIndex: 'giftBalance',
  //   width: '10%',
  // },
  {
    title: '余额/元',
    dataIndex: 'balance',
    width: '10%',
  },
  {
    title: '退卡审核操作',
    key: 'action',
    scopedSlots: {customRender: 'action'},
    width: '20%',
  },
];

export default {
  data() {
    return {
      data: [],
      pagination: {},
      loading: false,
      columns,
    };
  },
  computed: {
    ...mapGetters("merchant_zt", ["merchantID"]),  // 在计算属性中，对 mapGetters 进行初始化
    ...mapGetters("account", ["user"]),
  },
  mounted() {
    this.fetch();
  },
  methods: {
    handleTableChange(pagination, filters, sorter) {
      console.log(pagination);
      const pager = {...this.pagination};
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
      // 通过全局状态管理获取状态，这里获取登录的商家的 id
      this.loading = true;
      getRefundInfo(this.merchantID).then(response => {
        console.log(response.data)
        if (response.data.code === 0) {
          for (let i in response.data.data) {
            let refund = response.data.data[i]
            // 转换显示的预付卡价格
            if (refund.cardAmount === null || refund.cardAmount === undefined) {
              refund.cardAmount = "0.00"
            } else {
              refund.cardAmount = (refund.cardAmount / 100.00).toFixed(2)
            }
            // 转换显示的赠送金额
            if (refund.giftAmount === null || refund.giftAmount === undefined) {
              refund.giftAmount = "0.00"
            } else {
              refund.giftAmount = (refund.giftAmount / 100.00).toFixed(2)
            }
            // 转换显示的余额
            if (refund.balance === null || refund.balance === undefined) {
              refund.balance = "0.00"
            } else {
              refund.balance = (refund.balance / 100.00).toFixed(2)
            }

          }

          const pagination = {...this.pagination};
          // Read total count from server
          // pagination.total = data.totalCount;
          pagination.total = 200;
          this.loading = false;
          this.data = response.data.data;  // 赋值
          this.pagination = pagination;

        } else {
          this.loading = false;
          this.error = response.data.msg
          this.$message.error(this.error)
        }
      }).catch(error => console.log(error, "error")); // 失败的返回
    },

    // 通过退卡审核
    refundPassAction(record) {
      refundPass(record.payedCardId).then(({ data }) => {
        console.log(data)
        if (data.code === 0) {
          this.$message.success(data.msg, 2)  // 跳出信息，显示跳转成功
        } else {
          this.error = data.msg
          this.$message.error(this.error, 2)   // 失败的返回
        }
        // 重新获取数据，刷新表格
        this.fetch()
      }).catch(error => {console.log(error, "error")});


    },

    // 拒绝退卡审核
    refundRejectAction(record) {
      refundReject(record.payedCardId).then(({ data }) => {
        console.log(data)
        if (data.code === 0) {
          this.$message.success(data.msg, 2)
        } else {
          // 错误代码为 1
          this.error = data.msg
          this.loading = false
          this.$message.error(this.error, 2)
        }
        // 重新获取数据，刷新表格
        this.fetch()
      }).catch(error => {
        this.loading = false
        console.log(error, "error")
      });
    }

  },
};
</script>
