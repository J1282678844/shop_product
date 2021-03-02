<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">后台管理系统</div>
      <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
        <el-form-item prop="name">
          <el-input v-model="param.name" placeholder="name">
            <el-button slot="prepend" icon="el-icon-lx-people"></el-button>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            type="password"
            placeholder="password"
            v-model="param.password"
            @keyup.enter.native="submitForm()"
          >
            <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
          </el-input>
        </el-form-item>
        <div class="login-btn">
          <el-button type="primary" @click="submitForm()">登录</el-button>
        </div>
        <el-button size="mini" class="btn-add" @click="handleAdd()" style="margin-left: 20px">注册</el-button>
      </el-form>
    </div>
    <!-- 注册弹出框 -->
    <el-dialog title="用户注册" :visible.sync="editVisible" width="40%">
      <el-form :model="userForm"
               ref="userForm"
               label-width="150px" size="small">
        <el-form-item label="帐号：">
          <el-input v-model="userForm.name" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="密码：">
          <el-input v-model="userForm.password" placeholder="请输入密码" style="width: 250px" show-password></el-input>
        </el-form-item>
        <el-form-item label="姓名：">
          <el-input v-model="userForm.realName" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="生日：">
          <el-date-picker v-model="userForm.birthday" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="体重：">
          <el-input v-model="userForm.weight" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="头像：" prop="img">
          <single-upload v-model="userForm.img"></single-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="addUser">确 定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
  import SingleUpload from "../SingleUpload";
  //注册弹框
  const defaultUser = {
    name: "",
    realName: "",
    password: "",
    img: "",
    birthday: "",
    weight: "",
  };

  export default {
    name: "Login",
    components: {SingleUpload},
    data() {
      //验证用户名
      var checkName = (rule, value, callback) => {
        let userReg = /^[A-Za-z]{3,16}$/;
        if (!userReg.test(value)) {
          return callback(new Error("请输入3到16位字母的用户名"))
        } else {
          callback();
        }
      }
      //验证密码
      var checkPassword=(rule,value,callback)=>{
        let  passwordReg=/^[A-Za-z0-9]{6,16}$/;
        if(!passwordReg.test(value)){
          return callback(new Error("请输入6-16位数字或字母的密码"));
        }else{
          callback();
        }
      }
      return {
        userForm: Object.assign({}, defaultUser),
        editVisible: false,
        param: {
          name: 'dongdong',
          password: '123456',
        },
        rules: {
          name: [{validator: checkName, trigger: 'blur', required: true}],
          password: [{validator: checkPassword, trigger: 'blur', required: true}]
        },
      }
    },
    methods: {
      submitForm() {
        this.$refs.login.validate(valid => {
          if (valid) {
            this.$axios.post("http://localhost:8080/admin/user/login",this.$qs.stringify(this.param)).then(res=>{
              console.log(res);
              if(res.data.code == 200){
                //将token 存入浏览器中
                window.sessionStorage.setItem("user",JSON.stringify({"name":this.name,"token":res.data.data}));
                this.$message.success('登录成功');
                this.$router.push('/');
              }else{
                return this.$message.error('账号或密码错误');
              }
            })
          } else {
            this.$message.error('请输入账号和密码');
            return false;
          }
        });
      },
      //新增弹框
      handleAdd() {
        this.editVisible = true;
        this.titleName = "用户新增";
        this.userForm = Object.assign({}, defaultUser);
      },
      //新增
      addUser(){
        this.$axios.post("http://localhost:8080/admin/user/add",this.$qs.stringify(this.userForm)).then(res=>{
          if (res.data.code == 200){
            this.$message.success('注册成功');
            this.editVisible = false;
          }
        })
      },
    },
    mounted() {
    }
  }
</script>

<style>
  .login-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    background-image: url(../../assets/img/agfgf.jpg);
    background-size: 100%;
  }
  .ms-title {
    width: 100%;
    line-height: 50px;
    text-align: center;
    font-size: 20px;
    color: #fff;
    border-bottom: 1px solid #ddd;
  }
  .ms-login {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 350px;
    margin: -190px 0 0 -175px;
    border-radius: 5px;
    background: rgba(255, 255, 255, 0.3);
    overflow: hidden;
  }
  .ms-content {
    padding: 30px 30px;
  }
  .login-btn {
    text-align: center;
  }
  .login-btn button {
    width: 100%;
    height: 36px;
    margin-bottom: 10px;
  }
  .login-tips {
    font-size: 12px;
    line-height: 30px;
    color: #fff;
  }
</style>
