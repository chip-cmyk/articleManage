<script setup>
import { User, Lock } from "@element-plus/icons-vue"
import { ref } from "vue"
import { ElMessage } from "element-plus"
//控制注册与登录表单的显示， 默认显示注册
const isRegister = ref(false)

//数据模型
const registerData = ref({
  username: "",
  password: "",
  rePassword: "",
})

const checkRePassword = (rule, value, callback) => {
  if (value === "") {
    callback(new Error("请确认密码"))
  } else if (value !== registerData.value.password) {
    callback(new Error("两次密码不一致"))
  } else {
    callback()
  }
}

//定义表单校验规则

const rules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 6, max: 16, message: "请输入6-16位非空字符", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 16, message: "请输入6-16位非空字符", trigger: "blur" },
  ],
  rePassword: [{ validator: checkRePassword, trigger: "blur" }],
}

import { userRegisterService, userLoginService } from "@/api/user.js"

const register = async () => {
  let result = await userRegisterService(registerData.value)
  // if(result.code === 0){
  //     //成功
  //     alert(result.message ? result.message : '注册成功');
  // }else{
  //     //失败了
  //     alert('注册失败')
  // }
  //alert(result.message ? result.message : '注册成功')
  ElMessage.success(result.message ? result.message : "注册成功")
}

import { useRouter } from "vue-router"
import { useTokenStore } from "@/stores/token.js"

const router = useRouter()
const tokenStore = useTokenStore()
const login = async () => {
  await form.value.validate()
  let result = await userLoginService(registerData.value)
  ElMessage.success(result.message ? result.message : "登录成功")

  tokenStore.setToken(result.data)

  router.push("/")
}

const clearRegisterData = () => {
  registerData.value = {
    username: "",
    password: "",
    rePassword: "",
  }
}
</script>

<template>
  <el-row class="login-page">
    <el-col :span="12" class="bg"></el-col>
    <el-col :span="6" :offset="3" class="form">
      <!-- 注册表单 -->
      <el-form
        ref="form"
        size="large"
        autocomplete="off"
        v-if="isRegister"
        :model="registerData"
        :rules="rules"
      >
        <el-form-item>
          <h1>注册</h1>
        </el-form-item>
        <el-form-item prop="username">
          <el-input
            :prefix-icon="User"
            placeholder="请输入用户名"
            v-model="registerData.username"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入密码"
            v-model="registerData.password"
          ></el-input>
        </el-form-item>
        <el-form-item prop="rePassword">
          <el-input
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入再次密码"
            v-model="registerData.rePassword"
          ></el-input>
        </el-form-item>
        <!-- 注册按钮 -->
        <el-form-item>
          <el-button
            class="button"
            type="primary"
            auto-insert-space
            @click="register"
          >
            注册
          </el-button>
        </el-form-item>
        <el-form-item class="flex">
          <el-link
            type="info"
            :underline="false"
            @click="
              isRegister = false
              clearRegisterData()
            "
          >
            ← 返回
          </el-link>
        </el-form-item>
      </el-form>
      <!-- 登录表单 -->
      <el-form
        ref="form"
        size="large"
        autocomplete="off"
        v-else
        :model="registerData"
        :rules="rules"
      >
        <el-form-item>
          <h1>登录</h1>
        </el-form-item>
        <el-form-item prop="username">
          <el-input
            :prefix-icon="User"
            placeholder="请输入用户名"
            v-model="registerData.username"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            name="password"
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入密码"
            v-model="registerData.password"
          ></el-input>
        </el-form-item>
        <el-form-item class="flex">
          <div class="flex">
            <el-checkbox>记住我</el-checkbox>
            <el-link type="primary" :underline="false">忘记密码？</el-link>
          </div>
        </el-form-item>
        <!-- 登录按钮 -->
        <el-form-item>
          <el-button
            class="button"
            type="primary"
            auto-insert-space
            @click="login"
            >登录</el-button
          >
        </el-form-item>
        <el-form-item class="flex">
          <el-link
            type="info"
            :underline="false"
            @click="
              isRegister = true
              clearRegisterData()
            "
          >
            注册 →
          </el-link>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style lang="scss" scoped>
/* 样式 */
.login-page {
  height: 100vh;
  background-color: #fff;

  .bg {
    // background: url('@/assets/logo2.png') no-repeat 60% center / 240px auto,
    background: url("@/assets/logo.jpg") no-repeat center / cover;
    border-radius: 0 15px 15px 0;
  }

  .form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    user-select: none;

    .title {
      margin: 0 auto;
    }

    .button {
      width: 100%;
    }

    .flex {
      width: 100%;
      display: flex;
      justify-content: space-between;
    }
  }
}
</style>
