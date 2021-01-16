<template>
  <div>
    <div>
      <div class="crumbs">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>
            <i class="el-icon-lx-cascades"></i> 属性列表
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="container">
        <div class="handle-box">
          <el-button
            type="primary"
            icon="el-icon-circle-plus"
            class="handle-del mr10"
            @click="addAttr"
          >属性新增
          </el-button>
          <el-input v-model="query.name" placeholder="属性名称" class="handle-input mr10"></el-input>
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
          <el-table-column prop="name" label="属性简称" align="center"></el-table-column>
          <el-table-column prop="nameCH" label="属性名称" align="center"></el-table-column>
          <el-table-column prop="typeId" label="分类" align="center"></el-table-column>
          <el-table-column prop="type" label="类型" align="center"></el-table-column>
          <el-table-column prop="isSKU" label="是否SKU" align="center"></el-table-column>
          <el-table-column prop="isDel" label="是否显示" align="center"></el-table-column>


          <el-table-column label="操作" width="180" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary" icon="el-icon-edit" circle
                @click="handleEdit( scope.row)"
              >
              </el-button>
              <el-button
                type="danger" icon="el-icon-delete" circle
                @click="handleDelete( scope.row)"
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

    </div>
  </div>
</template>

<script>
    export default {
        name: "Attr",
        data() {
            return {
              query: {
                name:"",
                size: 5,
                current: 1
              },
              tableData: [],
              pageSizes: [5, 10, 15, 20],
              pageTotal: 0,

            }
        },
        methods: {

          getData:function () {
            this.$axios.get("http://localhost:8080/api/attr/getData?current="+this.query.current+"&size="+this.query.size+"&name="+this.query.name).then(res=>{
              this.tableData = res.data.data.list;
              this.pageTotal = res.data.data.count;
            }).then(err=>{

            })
          },

          //条件查询
          handleSearch:function(){
            this.getData();
          },
          //新增
          addAttr(){},

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
        },
        mounted() {
          this.getData();
        }
    }
</script>

<style scoped>

</style>
