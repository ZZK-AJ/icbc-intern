<template>
  <div>
    <h2>预付卡审核提交页</h2>

    <a-card :body-style="{padding: '24px 32px'}" :bordered="false">
      <a-form @submit="onSubmit" :form="form">

        <a-form-item :label="$t('预付卡名称')" :labelCol="{span: 7}" :wrapperCol="{span: 10}">
          <a-input v-decorator="['card_name', {rules: [{ required: true, message: '请输入预付卡名称', whitespace: true}]}]"/>
        </a-form-item>
        <a-form-item :label="$t('预付卡类型')" :labelCol="{span: 7}" :wrapperCol="{span: 10}" >
          <a-input v-decorator="['card_type', {rules: [{ required: true, message: '请输入预付卡类型', whitespace: true}]}]"/>
        </a-form-item>
        <a-form-item
            :label="$t('销售价格')"
            :labelCol="{span: 7}"
            :wrapperCol="{span: 10}"
        >
          <a-input v-decorator="['card_amount', {rules: [{ required: true, message: '请输入预付卡金额', whitespace: true}]}]"/>
        </a-form-item>
        <a-form-item :label="$t('赠送金额')" :labelCol="{span: 7}" :wrapperCol="{span: 10}" >
          <a-input v-decorator="['gift_amount', {rules: [{ required: true, message: '请输入预付卡赠送金额', whitespace: true}]}]"/>
        </a-form-item>
        <a-form-item :label="$t('消费折扣')" placeholder="扣款百分数 100代表原价 90代表9折" :labelCol="{span: 7}" :wrapperCol="{span: 10}" >
          <a-input v-decorator="['discount_rate', {rules: [{ required: true, message: '请输入预付卡消费折扣', whitespace: true}]}]"/>
        </a-form-item>

<!--        <a-form-item :label="$t('过期时间')" :labelCol="{span: 7}" :wrapperCol="{span: 10}" >-->
<!--          <a-input v-decorator="['expire_date', {rules: [{ required: true, message: '请输入预付卡消费过期时间', whitespace: true}]}]"/>-->
<!--        </a-form-item>-->

        <a-form-item label="过期时间" :labelCol="{span: 7}" :wrapperCol="{span: 10}">
          <a-select v-decorator="['expire_date', {rules: [{ required: true, message: '请输入预付卡消费过期时间'}] },]" placeholder="选择预付卡消费过期时间" @change="handleSelectChange">
            <a-select-option value="1"> 1年 </a-select-option>
            <a-select-option value="2"> 2年 </a-select-option>
            <a-select-option value="3"> 3年 </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item
            :label="$t('预付卡信息')"
            :labelCol="{span: 7}"
            :wrapperCol="{span: 10}"
            :required="false"
        >
          <a-textarea v-decorator="['card_info', {rules: [{ required: true, message: '预付卡信息', whitespace: true}]}]"/>
        </a-form-item>

        <a-form-item style="margin-top: 24px" :wrapperCol="{span: 15, offset: 7}">
          <a-button htmlType="submit" type="primary">提交审核</a-button>
        </a-form-item>
      </a-form>
    </a-card>

  </div>
</template>

<script>

import {mapGetters, mapMutations} from "vuex";
import { merchantReview} from "@/services/user";

export default {
  data () {
    return {
      error: '',
      form: this.$form.createForm(this)
    }
  },
  computed: {
    ...mapGetters("merchant_zt", ["merchantID"]),   // 在计算属性中，对 mapGetters 进行初始化
    ...mapGetters("account", ["user"]),
  },
  methods: {
    ...mapMutations("merchant_zt", ["setMerchantID"]),  // 在 methods 中对 mapMutations 初始化
    ...mapMutations('account', ['setUser', 'setPermissions', 'setRoles']),
    handleSelectChange(value) {
      console.log("handleSelectChange")
      console.log(value);
      this.form.setFieldsValue({
        expire_date: value
      })
    },
    onSubmit (e) {
      e.preventDefault()  // 阻止原生事件
      this.form.validateFields((err) => {  // 验证表单
        if (!err) {
          // 显示加载中
          this.logging = true
          // 获取表单用户名和密码
          this.card_name = this.form.getFieldValue('card_name')
          this.card_type = this.form.getFieldValue('card_type')
          this.card_amount = this.form.getFieldValue('card_amount') * 100  // 后端以分为单位
          this.gift_amount = this.form.getFieldValue('gift_amount') * 100
          this.discount_rate = this.form.getFieldValue('discount_rate')
          this.expire_date = this.form.getFieldValue('expire_date')
          this.card_info = this.form.getFieldValue('card_info')
          console.log(this.merchantID, this.card_name, this.card_type, this.card_amount, this.gift_amount, this.discount_rate, this.expire_date)
          // 执行登录请求 merchantReview 登录后触发 afterLogin 函数
          merchantReview(this.merchantID, this.card_name, this.card_type, this.card_info, this.card_amount, this.gift_amount, this.discount_rate, this.expire_date).then(this.afterSubmit)
        }
      })
    },
    afterSubmit(res) {
      this.logging = false
      const result = res.data
      console.log(result)

      if (result.code === 0) {
        this.$message.success(result.msg, 2)  // 跳出信息，显示跳转成功
      } else {
        this.error = result.message
      }
    },
    onClose() {
      this.error = false
    }
  },

}

</script>

<style>

</style>
