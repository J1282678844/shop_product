<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 用户列表
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
              <el-input v-model="query.name" class="input-width" placeholder="帐号/姓名"
                        clearable>
              </el-input>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
      <el-card class="operate-container" shadow="never">
        <i class="el-icon-tickets"></i>
        <span>数据列表</span>
        <el-button size="mini" class="btn-add" click="handleAdd()" style="margin-left: 20px">添加</el-button>
      </el-card>
      <div class="table-container">
        <el-table ref="adminTable"
                  :data="tableData"
                  style="width: 100%;"
                  v-loading="listLoading"
                  border>
          <el-table-column label="编号" width="100" align="center">
            <template slot-scope="scope">{{scope.row.id}}</template>
          </el-table-column>
          <el-table-column label="帐号" align="center">
            <template slot-scope="scope">{{scope.row.name}}</template>
          </el-table-column>
          <el-table-column label="姓名" align="center">
            <template slot-scope="scope">{{scope.row.realName}}</template>
          </el-table-column>
          <el-table-column label="邮箱" align="center">
            <template slot-scope="scope">{{scope.row.email}}</template>
          </el-table-column>
          <el-table-column label="添加时间" width="160" align="center">
            <template slot-scope="scope">{{scope.row.createDate | formatDateTime}}</template>
          </el-table-column>
          <el-table-column label="操作" width="180" align="center">
            <template slot-scope="scope">
              <el-button size="mini"
                         type="text"
                         click="handleSelectRole(scope.row)">分配角色
              </el-button>
              <el-button size="mini"
                         type="text"
                         click="handleUpdate(scope.row)">编辑
              </el-button>
              <el-button size="mini"
                         type="text"
                         click="handleDelete(scope.row)">删除
              </el-button>
            </template>
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
    <!--<el-dialog :title="titleName" :visible.sync="editVisible" width="40%">
      <el-form :model="userForm"
               ref="userForm"
               label-width="150px" size="small">
        <el-form-item label="帐号：">
          <el-input v-model="userForm.name" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="姓名：">
          <el-input v-model="userForm.realName" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="邮箱：">
          <el-input v-model="userForm.email" style="width: 250px"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" click="saveOrUpdateUser">确 定</el-button>
            </span>
    </el-dialog>-->
  </div>
</template>

<script>
    //查询的重置
    const resetQuery = {
      name:"",
      current:1,
      size:3
    };
    export default {
        name: "User",
        data() {
            return {
              query:Object.assign({},resetQuery),

              tableData: [],
              pageSizes: [3, 10, 15, 20],
              editVisible: false,
              pageTotal: 0,
              titleName: "",
            }
        },
        methods: {
          //查询用户
          getData(){
            this.$axios.get("http://localhost:8080/admin/user/getData?current=" + this.query.current + "&size=" + this.query.size + "&name=" + this.query.name).then(res=>{
              this.tableData = res.data.data.list;
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

<style scoped>

</style>
