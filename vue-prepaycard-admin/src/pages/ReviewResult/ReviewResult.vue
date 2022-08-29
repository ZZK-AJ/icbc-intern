<template>
  <div>
    <h2> 预付卡审核结果页 </h2>

    <a-table
        :columns="columns"
        :data-source="data"
        :loading="loading"
        @change="handleTableChange"
    >
    </a-table>
  </div>
</template>

<script>
import {getReviewResult} from '@/services/user'
import {mapGetters} from 'vuex'  // 状态管理，mapMutations 设置状态，mapGetters 获取状态

const columns = [
  {
    title: '预付卡名称',
    dataIndex: 'cardName',
    sorter: true,
    width: '10%',
    scopedSlots: { customRender: 'name' },
    key: "cardName",
  },
  {
    title: '预付卡类型',
    dataIndex: 'cardType',
    width: '10%',
    key: "cardType",
  },
  {
    title: '预付卡金额 /元',
    dataIndex: 'cardAmount',
    width: '10%',
    key: "cardAmount",
  },
  {
    title: '赠送金额 /元',
    dataIndex: 'giftAmount',
    width: '10%',
    key: "giftAmount",
  },
  {
    title: '消费折扣 /%',
    dataIndex: 'discountRate',
    width: '10%',
    key: "discountRate",
  },
  {
    title: '预付卡过期年限',
    dataIndex: 'expireDate',
    width: '10%',
    key: "expireDate",
  },
  {
    title: '审核结果',
    dataIndex: 'reviewStatus',
    width: '10%',
    key: "reviewStatus",
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
      // 通过全局状态管理获取状态，获取登录的商家的 id
      this.loading = true;
      getReviewResult(this.merchantID).then(({ data }) => {
        console.log(data)

        if (data.code === 0) {
          for (let i in data.data) {
            let mechantReview = data.data[i]
            console.log("getReviewResult")
            console.log(mechantReview)
            if (mechantReview.reviewStatus === 0) {
              mechantReview.reviewStatus = "审核中"
            } else if (mechantReview.reviewStatus === 1) {
              mechantReview.reviewStatus = "审核通过"
            } else {
              mechantReview.reviewStatus = "审核失败"
            }
            // 转换年份
            if (mechantReview.expireDate === "1") {
              mechantReview.expireDate = "1年"
            } else if (mechantReview.expireDate === "2") {
              mechantReview.expireDate = "2年"
            } else {
              mechantReview.expireDate = "3年"
            }
            // 转换显示的预付卡价格
            if (mechantReview.cardAmount === null || mechantReview.cardAmount === undefined) {
              mechantReview.cardAmount = "0.00"
            } else {
              mechantReview.cardAmount = (mechantReview.cardAmount / 100.00).toFixed(2)
            }
            // 转换显示的赠送金额
            if (mechantReview.giftAmount === null || mechantReview.giftAmount === undefined) {
              mechantReview.giftAmount = "0.00"
            } else {
              mechantReview.giftAmount = (mechantReview.giftAmount / 100.00).toFixed(2)
            }
            console.log(mechantReview)
          }

          const pagination = { ...this.pagination };
          // Read total count from server
          // pagination.total = data.totalCount;
          pagination.total = 200;
          this.loading = false;
          this.data = data.data;
          this.pagination = pagination;

        } else {
          this.error = data.msg
        }

      }).catch(error => console.log(error, "error")); // 失败的返回

    },
  },
};
</script>
