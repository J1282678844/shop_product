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
          <el-input v-model="query.name" placeholder="属性简称" class="handle-input mr10"></el-input>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
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
          <el-table-column prop="typeId" label="分类" align="center" :formatter="classFort"></el-table-column>
          <el-table-column prop="type" label="类型" align="center" :formatter="typeFort"></el-table-column>
          <el-table-column prop="isSKU" label="是否SKU" align="center" :formatter="skuFort"></el-table-column>
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

          <el-table-column label="操作" width="180" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary" icon="el-icon-edit" circle
                @click="handleEdit( scope.row)">
              </el-button>
              <el-button
                type="primary" icon="el-icon-search" circle
                @click="showValueTable=true">查看
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

      <!-- 编辑弹出框 -->
      <el-dialog :title="属性新增" :visible.sync="dialogAttrAdd" width="50%" v-if="dialogAttrAdd">
        <el-form ref="addForm" :model="addForm" label-width="140px">
          <el-form-item label="分类" prop="typeId">
            <el-select v-model="addForm.typeId" placeholder="请选择分类">
              <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="属性名称" prop="name">
            <el-input v-model="addForm.nameCH"></el-input>
          </el-form-item>
          <el-form-item label="属性简称" prop="name">
            <el-input v-model="addForm.name"></el-input>
          </el-form-item>
          <el-form-item label="属性类型">
            <el-radio-group v-model="addForm.type">
              <el-radio :label="0">下拉框</el-radio>
              <el-radio :label="1">单选框</el-radio>
              <el-radio :label="2">复选框</el-radio>
              <el-radio :label="3">输入框</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否SKU" prop="isDel">
            <el-radio-group v-model="addForm.isSKU">
              <el-radio :label="0">是</el-radio>
              <el-radio :label="1">否</el-radio>
            </el-radio-group>
          </el-form-item>

        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="closeDialog">取 消</el-button>
            <el-button type="primary" @click="addAttrForm">提 交</el-button>
        </span>
      </el-dialog>

      <el-dialog :title="属性修改" :visible.sync="dialogAttrUpdate" width="50%" v-if="dialogAttrUpdate">
        <el-form ref="addForm" :model="updateForm" label-width="140px">
          <el-form-item label="分类" prop="typeId">
            <el-select v-model="updateForm.typeId" placeholder="请选择分类">
              <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="属性名称" prop="name">
            <el-input v-model="updateForm.nameCH"></el-input>
          </el-form-item>
          <el-form-item label="属性简称" prop="name">
            <el-input v-model="updateForm.name"></el-input>
          </el-form-item>
          <el-form-item label="属性类型">
            <el-radio-group v-model="updateForm.type">
              <el-radio :label="0">下拉框</el-radio>
              <el-radio :label="1">单选框</el-radio>
              <el-radio :label="2">复选框</el-radio>
              <el-radio :label="3">输入框</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否SKU" prop="isDel">
            <el-radio-group v-model="updateForm.isSKU">
              <el-radio :label="0">是</el-radio>
              <el-radio :label="1">否</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="是否展示" prop="isDel">
            <el-radio-group v-model="updateForm.isDel">
              <el-radio :label="1">不展示</el-radio>
              <el-radio :label="0">展示</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="closeDialog">取 消</el-button>
            <el-button type="primary" @click="updateAttrForm">提 交</el-button>
        </span>
      </el-dialog>

      <el-dialog title="属性值信息" :visible.sync="showValueTable">
        <el-button
          type="primary"
          icon="el-icon-circle-plus"
          class="handle-del mr10"
          @click="addAttrValue"
        >属性值新增
        </el-button
        <el-table
          :data="valueData"
          border
          class="table">
          <el-table-column property="nameCH" label="属性名称" align="center"></el-table-column>
          <el-table-column property="value" label="属性值" align="center"></el-table-column>
          <el-table-column property="valueName" label="值名称" align="center"></el-table-column>
          <el-table-column label="操作" width="180" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary" icon="el-icon-edit" circle
                @click="updateValue( scope.row)">
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>

    </div>
  </div>
</template>

<script>
  export default {
    name: "Attr",
    data() {
      return {
        query: {
          name: "",
          size: 5,
          current: 1
        },
        search:{
          nameCH:"",
          size: 5,
          current: 1
        },
        tableData: [],
        valueData: [],
        pageSizes: [5, 10, 15, 20],
        pageTotal: 0,
        dialogAttrAdd: false,
        dialogAttrUpdate: false,
        showValueTable:false,
        addForm: {
          typeId: "",
          name: "",
          nameCH: "",
          type: "",
          isSKU: ""
        },
        updateForm: {
          typeId: "",
          name: "",
          nameCH: "",
          type: "",
          isDel: "",
          isSKU: ""
        },
        types: [
          {"id": 10, name: "分类/电子产品/手机"},
          {"id": 11, name: "分类/电子产品/音响"},
          {"id": 12, name: "分类/服饰/外套"},
          {"id": 13, name: "分类/服饰/下装"},
          {"id": 16, name: "分类/电子产品/摄像机"},
          {"id": 17, name: "分类/电子产品/电脑"}
        ],

      }
    },
    methods: {
      //属性值分页查询
      getValueData(){},
      //新增弹框
      addAttrValue(){},

      //修改弹框回显
      updateValue(row){},

      /*---------------------------------------------------------------------------------------------------------*/
      //属性分页查询
      getData() {
        this.$axios.get("http://localhost:8080/api/attr/getData?current=" + this.query.current + "&size=" + this.query.size + "&name=" + this.query.name).then(res => {
          this.tableData = res.data.data.list;
          this.pageTotal = res.data.data.count;
        })
      },
      //查询分类数据
      getTypeData() {
        this.$axios.get("http://localhost:8080/api/type/getData").then(res => {
          this.typeData = res.data.data;
        })
      },
      //初始化分类名称
      classFort(row, column) {
        for (let i = 0; i < this.typeData.length; i++) {
          if (row.typeId === this.typeData[i].id) {
            return this.typeData[i].name;
          }
        }
      },
      //初始化类型名称
      typeFort(row, column) {
        return row.type == 0 ? "下拉框" : row.type == 1 ? "单选框" : row.type == 2 ? "复选框" : row.type == 3 ? "输入框" : "";
      },
      //初始化SKU
      skuFort(row, column) {
        return row.isSKU == 0 ? "是" : "否";
      },
      //是否展示
      handleDelete(row) {
        let del = row.isDel;
        let params = {
          id: row.id,
          isDel: del
        }
        this.$axios.post("http://localhost:8080/api/attr/delete", this.$qs.stringify(params)).then(res => {
          this.getData();
        })
      },
      //条件查询
      handleQuery() {
        this.getData();
      },
      //新增弹框
      addAttr() {
        this.dialogAttrAdd = true;
        this.refreshForm();
      },
      //新增
      addAttrForm() {
        this.$axios.post("http://localhost:8080/api/attr/add", this.$qs.stringify(this.addForm)).then(res => {
          this.dialogAttrAdd = false;
          this.refreshForm();
          this.getData();
        })
      },
      //修改弹框回显
      handleEdit(row) {
        this.dialogAttrUpdate = true;
        this.$axios.get("http://localhost:8080/api/attr/getDataById?id=" + row.id).then(res => {
          this.updateForm = res.data.data;
        })
      },
      //修改
      updateAttrForm() {
        this.$axios.post("http://localhost:8080/api/attr/update", this.$qs.stringify(this.updateForm)).then(res => {
          this.dialogAttrUpdate = false;
          this.getData();
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
        this.dialogAttrAdd = false;
        this.dialogAttrUpdate = false;
      },
      refreshForm() {
        for (let key in this.updateForm) {
          this.updateForm[key] = ""
        }
      }
    },
    mounted() {
      this.getData();
      this.getTypeData();
      this.getValueData();
    }
  }
</script>

<style scoped>

</style>
