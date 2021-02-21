<template>
  <div>
    <div>
      <div class="crumbs">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>
            <i class="el-icon-lx-cascades"></i> 品牌列表
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="container">
        <div class="handle-box">
          <el-button
            type="primary"
            icon="el-icon-circle-plus"
            class="handle-del mr10"
            @click="addBrand"
          >品牌新增
          </el-button>
          <el-input v-model="query.name" placeholder="品牌名称" class="handle-input mr10"></el-input>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
        </div>
        <el-table
          :data="tableData"
          border
          class="table"
          ref="multipleTable"
          header-cell-class-name="table-header"
        >
          <el-table-column type="selection" width="55" align="center"></el-table-column>
          <el-table-column prop="id" label="编号" width="55" align="center"></el-table-column>
          <el-table-column prop="name" label="品牌名称" align="center"></el-table-column>
          <el-table-column prop="brandE" label="首字母" align="center"></el-table-column>
          <el-table-column prop="brandDesc" label="品牌故事" width="300" align="center"></el-table-column>
          <el-table-column prop="ord" label="排序" align="center"></el-table-column>

          <el-table-column label="品牌logo" align="center">
            <template slot-scope="scope">
              <el-image
                class="table-td-thumb"
                :src="scope.row.imgpath"
                :preview-src-list="[scope.row.imgpath]"
              ></el-image>
            </template>
          </el-table-column>

          <el-table-column label="是否显示" width="100" align="center">
            <template slot-scope="scope">
              <el-switch
                @change="handleDelete(scope.row)"
                :active-value="0"
                :inactive-value="1"
                v-model="scope.row.isDel">
              </el-switch>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="150" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary" icon="el-icon-edit" circle
                @click="handleEdit(scope.row)"
              >
              </el-button>
            </template>
          </el-table-column>
        </el-table>


        <div class="pagination">
          <el-pagination
            background
            layout="total, sizes, prev, pager, next, jumper"
            :current-page="query.current"
            :page-size="query.size"
            :page-sizes="pageSizes"
            :total="pageTotal"
            @current-change="handlePageChange"
            @size-change="handleSizeChange"
          ></el-pagination>
        </div>

      </div>

      <!------------------------------------------ 新增弹出框 ---------------------------------------------->
      <el-dialog title="新增信息" width="40%" :visible.sync="dialogBrandAdd">
        <el-form ref="brandForm" :rules="rules" :model="brandForm" label-width="120px">
          <el-form-item label="品牌名称" prop="name">
            <el-input v-model="brandForm.name" size="small" style="width: 300px" clearable></el-input>
          </el-form-item>
          <el-form-item label="品牌首字母" prop="brandE">
            <el-input v-model="brandForm.brandE" size="small" style="width: 300px" clearable></el-input>
          </el-form-item>

          <el-form-item label="品牌LOGO" prop="imgpath">
            <single-upload v-model="brandForm.imgpath"></single-upload>
          </el-form-item>

          <el-form-item label="品牌故事" prop="brandDesc">
            <el-input
              type="textarea"
              :rows="2"
              maxlength="200"
              show-word-limit
              placeholder="请输入品牌故事"
              v-model="brandForm.brandDesc">
            </el-input>
          </el-form-item>
          <el-form-item label="排序" prop="ord">
            <el-input-number v-model="brandForm.ord" :min="0" :max="100"></el-input-number>
          </el-form-item>

        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="closeDialog">取 消</el-button>
            <el-button type="primary" @click="addPinpai">确 定</el-button>
        </span>
      </el-dialog>

      <!------------------------------------------ 修改弹出框 ---------------------------------------------->
      <el-dialog title="修改信息" width="40%" :visible.sync="dialogBrandUpdate">
        <el-form ref="brandForm" :model="formUpdate" label-width="120px">
          <el-form-item label="品牌名称" prop="name">
            <el-input v-model="formUpdate.name" size="small" style="width: 300px" clearable></el-input>
          </el-form-item>
          <el-form-item label="品牌首字母" prop="brandE">
            <el-input v-model="formUpdate.brandE" size="small" style="width: 300px" clearable></el-input>
          </el-form-item>

          <el-form-item label="品牌LOGO" prop="imgpath">
            <single-upload v-model="formUpdate.imgpath"></single-upload>
          </el-form-item>

          <el-form-item label="品牌故事" prop="brandDesc">
            <el-input
              type="textarea"
              :rows="2"
              maxlength="200"
              show-word-limit
              placeholder="请输入品牌故事"
              v-model="formUpdate.brandDesc"
              size="small" style="width: 350px" clearable>
            </el-input>
          </el-form-item>
          <el-form-item label="排序" prop="ord">
            <el-input-number v-model="formUpdate.ord" :min="0" :max="100"></el-input-number>
          </el-form-item>

          <el-form-item label="是否展示" prop="isDel">
            <el-radio-group v-model="formUpdate.isDel">
              <el-radio :label="0">是</el-radio>
              <el-radio :label="1">否</el-radio>
            </el-radio-group>
          </el-form-item>

        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="closeDialog">取 消</el-button>
            <el-button type="primary" @click="updatePinpai">修 改</el-button>
        </span>
      </el-dialog>

    </div>
  </div>
</template>

<script>
  import SingleUpload from "../SingleUpload";

  export default {
    name: "Brand",
    components: {SingleUpload},
    data() {
      return {
        query: {
          name: "",
          size: 5,
          current: 1
        },

        tableData: [],
        pageSizes: [5, 10, 15, 20],
        pageTotal: 0,

        dialogBrandAdd: false,
        dialogBrandUpdate: false,

        titleName: "",
        imageUrl: '',
        brandForm: {
          name: "",
          brandE: "",
          ord: "",
          imgpath: "",
          brandDesc: ""
        },
        rules:{
          name: [
            { required: true, message: '请输入品牌名称', trigger: 'blur' },
            { min: 2, max: 8, message: '长度在 2 到 8 个字符', trigger: 'blur' }
          ],
          brandE: [
            { required: true, message: '请输入品牌首字母', trigger: 'blur' },
          ],
        },
        formUpdate: {
          name: "",
          brandE: "",
          ord: "",
          imgpath: "",
          brandDesc: "",
          isDel: 1
        }
      }
    },
    methods: {
      //分页查询
      getData() {
        this.$axios.get("http://localhost:8080/api/brand/getData?current=" + this.query.current + "&size=" + this.query.size + "&name=" + this.query.name).then(res => {
          this.tableData = res.data.data.list;
          this.pageTotal = res.data.data.count;
        })
      },
      //条件查询
      handleSearch() {
        this.getData();
      },
      //是否展示
      handleDelete(row) {
        let del = row.isDel;
        let params = {
          id: row.id,
          isDel: del
        }
        this.$axios.post("http://localhost:8080/api/brand/delete", this.$qs.stringify(params)).then(res => {
          this.getData();
        })
      },
      //回显
      handleEdit(row) {
        this.dialogBrandUpdate = true;
        this.$axios.get("http://localhost:8080/api/brand/getDataById?id=" + row.id).then(res => {
          this.formUpdate = res.data.data;
        })
      },
      //修改
      updatePinpai() {
        this.$axios.post("http://localhost:8080/api/brand/update", this.$qs.stringify(this.formUpdate)).then(res => {
          this.dialogBrandUpdate = false;
          this.getData();
        })
      },
      //新增方法
      addBrand() {
        this.dialogBrandAdd = true;
        this.refreshForm();
      },
      //新增
      addPinpai() {
        //取验证结果
        this.$refs["brandForm"].validate((valid) => {
          if (valid){
            this.$axios.post("http://localhost:8080/api/brand/add", this.$qs.stringify(this.brandForm)).then(res => {
              this.dialogBrandAdd = false;
              this.refreshForm();
              this.getData();
            })
          }
        })
      },

      //上下页
      handlePageChange(val) {
        this.$set(this.query, 'current', val);
        this.getData();
      },
      //条数显示
      handleSizeChange(val) {
        this.$set(this.query, 'current', 1);
        this.$set(this.query, 'size', val);
        this.getData();
      },
      //关闭dialog清空表单
      closeDialog() {
        this.refreshForm();
        this.dialogBrandAdd = false;
        this.dialogBrandUpdate = false;
      },
      refreshForm() {
        for (let key in this.brandForm) {
          this.brandForm[key] = ""
        }
      }
    },
    mounted() {
      this.getData();
    }
  }
</script>

<style>
  .handle-box {
    margin-bottom: 20px;
  }
  .handle-select {
    width: 120px;
  }
  .handle-input {
    width: 300px;
    display: inline-block;
  }
  .table {
    width: 100%;
    font-size: 14px;
  }
  .red {
    color: #ff0000;
  }
  .mr10 {
    margin-right: 10px;
  }
  .table-td-thumb {
    display: block;
    margin: auto;
    width: 40px;
    height: 40px;
  }
</style>
