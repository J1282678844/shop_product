<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 日志记录
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="app-container">
      <el-card class="filter-container" shadow="never">
        <div>
          <i class="el-icon-search"></i>
          <span>筛选搜索</span>
          <el-button
            style="float:right"
            type="primary"
            @click="handleSearchList()"
            size="small">
            查询搜索
          </el-button>
          <el-button
            style="float:right;margin-right: 15px"
            @click="handleResetSearch()"
            size="small">
            重置
          </el-button>
        </div>
        <div style="margin-top: 15px">
          <el-form :inline="true" :model="query" size="small" label-width="140px">
            <el-form-item label="输入搜索：">
              <el-input v-model="query.realName" class="input-width" placeholder="操作人姓名"
                        clearable>
              </el-input>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
      <el-card class="operate-container" shadow="never">
        <i class="el-icon-tickets"></i>
        <span>日志列表</span>
      </el-card>
      <div class="table-container">
        <el-table ref="adminTable"
                  :data="tableData"
                  style="width: 100%;"
                  border>
          <el-table-column label="操作人ID" width="70" align="center">
            <template slot-scope="scope">{{scope.row.userId}}</template>
          </el-table-column>
          <el-table-column label="操作人姓名" width="90" align="center">
            <template slot-scope="scope">{{scope.row.realName}}</template>
          </el-table-column>
          <el-table-column label="操作时间" width="140" align="center">
            <template slot-scope="scope">{{scope.row.createTime}}</template>
          </el-table-column>
          <el-table-column label="状态" width="50" align="center" :formatter="isType">
            <template slot-scope="scope">{{scope.row.type}}</template>
          </el-table-column>
          <el-table-column label="访问的类路径" width="180" align="center" show-overflow-tooltip>
            <template slot-scope="scope">{{scope.row.classname}}</template>
          </el-table-column>
          <el-table-column label="访问的方法名" width="100" align="center">
            <template slot-scope="scope">{{scope.row.methodName}}</template>
          </el-table-column>
          <el-table-column label="访问的参数" width="160" align="center" show-overflow-tooltip>
            <template slot-scope="scope">{{scope.row.args}}</template>
          </el-table-column>
          <el-table-column label="描述信息" width="120" align="center" show-overflow-tooltip>
            <template slot-scope="scope">{{scope.row.describeInfo}}</template>
          </el-table-column>
          <el-table-column label="异常信息" align="center" show-overflow-tooltip>
            <template slot-scope="scope">{{scope.row.errorInfo}}</template>
          </el-table-column>
        </el-table>
        <div class="pagination-container">
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
    </div>
    <!-- 编辑弹出框 -->
    <!--<el-dialog title="修改用户信息" :visible.sync="editVisible" width="40%">
      <el-form :model="userForm"
               ref="userForm"
               label-width="150px" size="small">
        <el-form-item label="帐号：">
          <el-input v-model="userForm.name" style="width: 250px"></el-input>
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
                <el-button type="primary" @click="updateUser">确 定</el-button>
            </span>
    </el-dialog>-->
  </div>
</template>

<script>
    //查询的重置
    const resetQuery = {
      realName:"",
      current:1,
      size:3
    };
    export default {
        name: "User",
        data() {
            return {
              query:Object.assign({},resetQuery),
              /*userForm: {
                name: "",
                realName: "",
                password: "",
                img: "",
                birthday: "",
                weight: "",
              },*/
              tableData: [],
              pageSizes: [3, 10, 15, 20],
              pageTotal: 0,
              editVisible: false,
              titleName: "",
            }
        },
        methods: {
          //查询用户
          getData(){
            this.$axios.get("http://localhost:8080/admin/logs/getData?current=" + this.query.current + "&size=" + this.query.size + "&realName=" + this.query.realName).then(res=>{
              console.log(res);
              this.tableData = res.data.data.data;
              this.pageTotal = res.data.data.count;
            })
          },
          //条件查询
          handleSearchList() {
            this.$set(this.query, 'current', 1);
            this.getData();
          },
          //条件查询的重置
          handleResetSearch() {
            this.query = Object.assign({}, resetQuery);
          },
          //初始化状态
          isType(row,column) {
            return row.type == 1 ? "正常" : "异常";
          },
          //上下页改变
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
