<template>
  <common-layout>
    <div class="top">
      <div class="header">
        <img alt="logo" class="logo" src="@/assets/img/logo.png" />
        <span class="title">{{systemName}}</span>
      </div>
    </div>

    <div class="login">
      <a-form @submit="onSubmit" :form="form">
        <a-tabs size="large" :tabBarStyle="{textAlign: 'center'}" style="padding: 0 2px;">
          <a-tab-pane tab="商家登录" key="1">
            <a-alert type="error" :closable="true" v-if="error" :message="error" @close='onClose' showIcon style="margin-bottom: 24px;" />
            <a-form-item>
              <a-input
                autocomplete="autocomplete"
                size="large"
                placeholder="mzk"
                v-decorator="['name', {rules: [{ required: true, message: '请输入账户', whitespace: true}]}]"
              >
                <a-icon slot="prefix" type="user" />
              </a-input>
            </a-form-item>
            <a-form-item>
              <a-input
                size="large"
                placeholder="123"
                autocomplete="autocomplete"
                type="password"
                v-decorator="['password', {rules: [{ required: true, message: '请输入密码', whitespace: true}]}]"
              >
                <a-icon slot="prefix" type="lock" />
              </a-input>
            </a-form-item>
          </a-tab-pane>

<!--          <a-tab-pane tab="运营方登录" key="2">-->
<!--            <a-form-item>-->
<!--              <a-input size="large" placeholder="mobile number" >-->
<!--                <a-icon slot="prefix" type="mobile" />-->
<!--              </a-input>-->
<!--            </a-form-item>-->
<!--            <a-form-item>-->
<!--              <a-row :gutter="8" style="margin: 0 -4px">-->
<!--                <a-col :span="16">-->
<!--                  <a-input size="large" placeholder="captcha">-->
<!--                    <a-icon slot="prefix" type="mail" />-->
<!--                  </a-input>-->
<!--                </a-col>-->
<!--                <a-col :span="8" style="padding-left: 4px">-->
<!--                  <a-button style="width: 100%" class="captcha-button" size="large">获取验证码</a-button>-->
<!--                </a-col>-->
<!--              </a-row>-->
<!--            </a-form-item>-->
<!--            <a-form-item>-->
<!--              <a-input autocomplete="autocomplete" size="large" placeholder="icbc"-->
<!--                  v-decorator="['operator_name', {rules: [{ required: true, message: '请输入运营方账户名', whitespace: true}]}]"-->
<!--              >-->
<!--                <a-icon slot="prefix" type="user" />-->
<!--              </a-input>-->
<!--            </a-form-item>-->
<!--            <a-form-item>-->
<!--              <a-input size="large" placeholder="1" autocomplete="autocomplete" type="password"-->
<!--                  v-decorator="['operator_password', {rules: [{ required: true, message: '请输入密码', whitespace: true}]}]"-->
<!--              >-->
<!--                <a-icon slot="prefix" type="lock" />-->
<!--              </a-input>-->
<!--            </a-form-item>-->

<!--          </a-tab-pane>-->
        </a-tabs>
        <div>
          <a-checkbox :checked="true" >自动登录</a-checkbox>
          <a style="float: right">忘记密码</a>
        </div>
        <a-form-item>
          <a-button :loading="logging" style="width: 100%;margin-top: 24px" size="large" htmlType="submit" type="primary">登录</a-button>
        </a-form-item>
        <div>
          其他登录方式
          <a-icon class="icon" type="alipay-circle" />
          <a-icon class="icon" type="taobao-circle" />
          <a-icon class="icon" type="weibo-circle" />
          <router-link style="float: right" to="/dashboard/workplace" >注册账户</router-link>
        </div>
      </a-form>
    </div>
  </common-layout>
</template>

<script>
import CommonLayout from '@/layouts/CommonLayout'
import {getMerchantId, merchantLogin} from '@/services/user'
// import {getRoutesConfig} from '@/services/user'
import {AUTH_TYPE, setAuthorization} from '@/utils/request'
// import {loadRoutes} from '@/utils/routerUtil'
import {mapGetters, mapMutations} from 'vuex'


export default {
  name: 'Login',
  components: {CommonLayout},
  data () {
    return {
      logging: false,
      error: '',
      form: this.$form.createForm(this)
    }
  },
  computed: {
    ...mapGetters("merchant_zt", ["merchantID"]),   // 在计算属性中，对 mapGetters 进行初始化
    ...mapGetters("account", ["user"]),
    systemName () {
      return this.$store.state.setting.systemName
    }
  },
  methods: {
    ...mapMutations("merchant_zt", ["setMerchantID"]),  // 在 methods 中对 mapMutations 初始化
    ...mapMutations('account', ['setUser', 'setPermissions', 'setRoles']),
    onSubmit (e) {
      e.preventDefault()  // 阻止原生事件
      this.form.validateFields((err) => {  // 验证表单
        if (!err) {
          // 显示加载中
          this.logging = true
          // 获取用户名和密码
          this.merchantName = this.form.getFieldValue('name')
          const password = this.form.getFieldValue('password')

          const operatorName = this.form.getFieldValue('operator_name')
          const operatorPassword = this.form.getFieldValue('operator_password')
          console.log(operatorName, operatorPassword)

          console.log(this.merchantName, password)
          // 执行登录请求 merchantLogin 登录后触发 afterLogin 函数
          merchantLogin(this.merchantName, password).then(this.afterLogin)
        }
      })
    },
    afterLogin(res) {
      this.logging = false
      const loginRes = res.data
      console.log(loginRes)

      if (loginRes.code === 0) {
        // 模拟前端可以接受的数据
        let result = {};
        result.code = 0
        result.message = ''
        result.data = {}
        result.data.user = {
          name: 'zk',
        }
        result.data.token = 'Authorization:' + Math.random()
        result.data.expireAt = new Date(new Date().getTime() + 30 * 60 * 1000)
        result.data.permissions = [{ id: 'queryForm', operation: ['add', 'edit'] }]
        result.data.roles = [{ id: 'admin', operation: ['add', 'edit', 'delete'] }]
        console.log(result)

        // 从返回数据中解析 用户 权限 角色
        const {user, permissions, roles} = result.data
        this.setUser(user)  // 设置用户
        this.setPermissions(permissions)  // 设置权限
        this.setRoles(roles)  // 设置 角色
        // todo 设置授权，每个请求都要用到 token 为后端返回的 token，这里似乎是登录后就全局设置了，手动设置的没有生效
        setAuthorization({token: loginRes.data, expireAt: new Date(result.data.expireAt)}, AUTH_TYPE.BASIC)

        // login 之后，需要获取这商户的 id ，用于后续查询
        console.log("token: " + loginRes.data)
        getMerchantId(this.merchantName).then(response => {
          // 保存 merchantId
          this.setMerchantID(response.data.data.id)
          // localStorage.setItem("merchantId", String(response.data.data.id))
          console.log("merchantID: " + this.merchantID)
        }).catch(error => console.log(error, "error")); // 失败的返回

        // todo 这里直接设定了菜单的 menuRoutes
        // let menuRoutes = [
        //   {
        //     "path": "ReviewResult",
        //     "name": "审核结果",
        //     "meta": {
        //       "icon": "dashboard",
        //       "authority": {
        //         "permission": "*"
        //       },
        //       "pAuthorities": [
        //         {
        //           "permission": "*"
        //         }
        //       ]
        //     },
        //     "fullPath": "/ReviewResult"
        //   },
        //   {
        //     "path": "SubmitReview",
        //     "name": "审核提交",
        //     "meta": {
        //       "icon": "form",
        //       "authority": {
        //         "permission": "*"
        //       },
        //       "pAuthorities": [
        //         {
        //           "permission": "*"
        //         }
        //       ]
        //     },
        //     "fullPath": "/SubmitReview"
        //   },
        //   {
        //     "path": "CardDetail",
        //     "name": "预付卡总览",
        //     "meta": {
        //       "icon": "dashboard",
        //       "authority": {
        //         "permission": "*"
        //       },
        //       "pAuthorities": [
        //         {
        //           "permission": "*"
        //         }
        //       ]
        //     },
        //     "fullPath": "/CardDetail"
        //   },
        //   {
        //     "path": "RefundReview",
        //     "name": "退卡审核",
        //     "meta": {
        //       "icon": "dashboard",
        //       "authority": {
        //         "permission": "*"
        //       },
        //       "pAuthorities": [
        //         {
        //           "permission": "*"
        //         }
        //       ]
        //     },
        //     "fullPath": "/RefundReview"
        //   }
        // ]
        // this.$store.commit('setting/setMenuData', menuRoutes)

        this.$router.push('/ReviewResult')  // 路由跳转
        this.$message.success(loginRes.msg, 2)  // 跳出信息，显示跳转成功

      } else {
        this.error = loginRes.message
      }
    },
    onClose() {
      this.error = false
    }
  },

}
</script>

<style lang="less" scoped>
  .common-layout{
    .top {
      text-align: center;
      .header {
        height: 44px;
        line-height: 44px;
        a {
          text-decoration: none;
        }
        .logo {
          height: 44px;
          vertical-align: top;
          margin-right: 16px;
        }
        .title {
          font-size: 33px;
          color: @title-color;
          font-family: 'Myriad Pro', 'Helvetica Neue', Arial, Helvetica, sans-serif;
          font-weight: 600;
          position: relative;
          top: 2px;
        }
      }
      .desc {
        font-size: 14px;
        color: @text-color-second;
        margin-top: 12px;
        margin-bottom: 40px;
      }
    }
    .login{
      width: 368px;
      margin: 0 auto;
      @media screen and (max-width: 576px) {
        width: 95%;
      }
      @media screen and (max-width: 320px) {
        .captcha-button{
          font-size: 14px;
        }
      }
      .icon {
        font-size: 24px;
        color: @text-color-second;
        margin-left: 16px;
        vertical-align: middle;
        cursor: pointer;
        transition: color 0.3s;

        &:hover {
          color: @primary-color;
        }
      }
    }
  }
</style>
