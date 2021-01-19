<template>
  <div>
    <h1>商品分类管理</h1>
    <el-tree
      :data="data"
      :props="defaultProps"
      show-checkbox
      node-key="id"
      :default-expand-all="true"
      :expand-on-click-node="false">
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
            <el-button
              type="text"
              size="mini"
              @click="() => append(data)">
            增加
          </el-button>
            <el-button
              type="text"
              size="mini"
              @click="() => edit(node,data)">
            编辑
          </el-button>
        </span>
      </span>
    </el-tree>
    <!------------------------------------------新增子菜单对话框------------------------------------------->
    <el-dialog
      title="新增"
      :visible.sync="addCategoryByParentIdDialogVisible"
      width="30%">
      <el-form ref="form" :rules="rules" :model="addForm" label-width="80px">
        <el-form-item label="上级名称">
          <input type="hidden" v-model="addForm.pid"></input>
          <el-input v-model="addForm.label" autocomplete="off" disabled="true"
                    size="small" style="width: 300px" clearable></el-input>
        </el-form-item>
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="addForm.name" autocomplete="off"
                    size="small" style="width: 300px" clearable></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addCategoryByParentIdDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitCategoryByParentId">确 定</el-button>
      </div>
    </el-dialog>

    <!--------------------------------------------编辑节点对话框-------------------------------------------->
    <el-dialog
      title="编辑"
      :visible.sync="editCategoryByParentIdDialogVisible"
      width="30%">
      <el-form ref="form" :model="updateForm" label-width="80px">
        <el-form-item label="分类名称">
          <el-input v-model="updateForm.name" size="small" style="width: 300px" clearable></el-input>
        </el-form-item>
        <el-form-item label="是否展示">
          <el-radio v-model="updateForm.isDel" label="0" border >是</el-radio>
          <el-radio v-model="updateForm.isDel" label="1" border >否</el-radio>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
      <el-button @click="editCategoryByParentIdDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="submitEditCategoryById">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  export default {
    name: "Type",
    data() {
      return {
        data: [],  //tree的数据
        defaultProps: {},
        axiosData: [], //将数据设为全局
        jsonStr: "",  //递归拼接
        addForm: {
          pid:"",
          label:"",
          name: ""
        },
        rules:{
          name: [
            { required: true, message: '请输入分类名称', trigger: 'blur' },
            { min: 3, max: 5, message: '长度在 2 到 8 个字符', trigger: 'blur' }
          ],
        },
        addCategoryByParentIdDialogVisible: false,
        updateForm:{
          name:"",
          id:"",
          isDel:"0"
        },
        editCategoryByParentIdDialogVisible: false
      }
    },
    methods: {
      //请求查询数据库数据
      getTree() {
        this.$axios.get("http://localhost:8080/api/type/getData").then(res => {
          this.axiosData = res.data.data;//将数据设为全局
          this.changeData();//转换成tree的数据data
        }).catch(err => console.log(err));
      },
      //判断是否为父节点（pId有没有指向当前id）
      isParent(node) {
        for (let i = 0; i < this.axiosData.length; i++) {
          if (node.id == this.axiosData[i].pid) {
            return true;
          }
        }
        return false;
      },//递归
      diguiNode(node) {
        //判断父节点
        var parent = this.isParent(node);
        if (parent == true) {
          //拼接父节点
          this.jsonStr += '{"id":' + node.id + ',"label":"' + node.name + '","children":[';
          //拼接子节点
          //先判断是否为当前节点的子节点
          var count = 0;
          for (let i = 0; i < this.axiosData.length; i++) {
            if (node.id == this.axiosData[i].pid) {
              count++;
              this.diguiNode(this.axiosData[i]);
              //“，”分隔每一个节点数据
              this.jsonStr += ",";
            }
          }
          //截取最后的“，”
          if (count != 0) {
            this.jsonStr = this.jsonStr.substr(0, this.jsonStr.length - 1);
          }
          //拼接完整
          this.jsonStr += ']}';
        } else {
          this.jsonStr += '{"id":' + node.id + ',"label":"' + node.name + '"}';
        }
      },
      //转换成tree的数据data
      changeData() {
        //判断顶层节点
        for (let i = 0; i < this.axiosData.length; i++) {
          if (this.axiosData[i].pid == 0) {
            this.diguiNode(this.axiosData[i]);
            break;
          }
        }
        console.log(this.jsonStr);
        //将字符串转为JSON对象
        this.data.push(JSON.parse(this.jsonStr));
      },
      //新增弹框
      append(data) {
        this.addForm.pid = data.id;
        this.addForm.label = data.label;
        this.addCategoryByParentIdDialogVisible = true;
      },
      //新增
      submitCategoryByParentId() {
        //取验证结果
        this.$refs["addForm"].validate((valid) => {
          if (valid) {
            this.$axios.post("http://localhost:8080/api/type/add",this.$qs.stringify(this.addForm)).then(res => {
              this.addCategoryByParentIdDialogVisible = false;
              //this.addForm = {};
              location.reload();
            })
          }
        })
      },

      //修改弹框
      edit(node,data) {
        this.updateForm.name = data.label;
        this.updateForm.id = data.id;
        this.editCategoryByParentIdDialogVisible = true;
      },
      //修改
      submitEditCategoryById() {
        this.$axios.post("http://localhost:8080/api/type/update",this.$qs.stringify(this.updateForm)).then(res=>{
          this.editCategoryByParentIdDialogVisible = false;
          location.reload();
        })
      }

    },
    mounted() {
      this.getTree();
    }
  }
</script>

<style scoped>
  .el-tree {
    margin-top: 20px;
  }

  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 20px;
    padding-right: 50px;
  }
</style>
