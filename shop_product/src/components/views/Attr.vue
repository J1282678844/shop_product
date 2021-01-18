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
                @click="showValueTableDiv(scope.row)">查看属性值
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

      <!-------------------------------------属性新增弹框维护------------------------------------->
      <el-dialog title="属性新增" :visible.sync="dialogAttrAdd" width="50%" v-if="dialogAttrAdd">
        <el-form ref="addForm" :model="addForm" label-width="140px">
          <el-form-item label="分类" prop="typeId">
            <el-select v-model="addForm.typeId" placeholder="请选择分类">
              <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="属性名称" prop="name">
            <el-input v-model="addForm.nameCH" size="small" style="width: 300px" clearable></el-input>
          </el-form-item>
          <el-form-item label="属性简称" prop="name">
            <el-input v-model="addForm.name" size="small" style="width: 300px" clearable></el-input>
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
            <el-button @click="closeDialog">取 消</el-button>
            <el-button type="primary" @click="addAttrForm">提 交</el-button>
        </span>
      </el-dialog>

      <!-------------------------------------属性修改弹框维护------------------------------------->
      <el-dialog title="属性修改" :visible.sync="dialogAttrUpdate" width="50%" v-if="dialogAttrUpdate">
        <el-form ref="addForm" :model="updateForm" label-width="140px">
          <el-form-item label="分类" prop="typeId">
            <el-select v-model="updateForm.typeId" placeholder="请选择分类">
              <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="属性名称" prop="name">
            <el-input v-model="updateForm.nameCH" size="small" style="width: 300px" clearable></el-input>
          </el-form-item>
          <el-form-item label="属性简称" prop="name">
            <el-input v-model="updateForm.name" size="small" style="width: 300px" clearable></el-input>
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
              <el-radio :label="0">是</el-radio>
              <el-radio :label="1">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="closeDialog">取 消</el-button>
            <el-button type="primary" @click="updateAttrForm">提 交</el-button>
        </span>
      </el-dialog>

      <!--------------------------------------属性值弹框列表------------------------------------------->
      <el-dialog :title="valueTitle" :visible.sync="showValueTable" width="35%" center>
        <div class="handle-box">
          <el-button type="primary"
                     icon="el-icon-circle-plus"
                     class="handle-del mr10"
                     @click="addAttrValue"
                     v-show="!showValueUpdateForm">
            属性值新增
          </el-button>
        </div>
        <el-table :data="valueData" border class="table" v-if="!showValueAddForm" v-show="!showValueUpdateForm">
          <el-table-column property="value" label="属性值" align="center"></el-table-column>
          <el-table-column property="valueName" label="值名称" align="center"></el-table-column>
          <el-table-column label="是否显示" width="100" align="center">
            <template slot-scope="scope">
              <el-switch
                @change="handleValue(scope.row)"
                :active-value="0"
                :inactive-value="1"
                v-model="scope.row.isDel">
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary" icon="el-icon-edit" circle
                @click="updateAttrValue( scope.row)">
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination" v-if="!showValueAddForm" v-show="!showValueUpdateForm">
          <el-pagination
            background
            layout="total, sizes, prev, pager, next, jumper"
            :current-page="search.current"
            :page-size="search.size"
            :page-sizes="pageSizess"
            :total="pageTotals"
            @current-change="handlePage"
            @size-change="handleSize"
          ></el-pagination>
        </div>

        <!----------------------------属性值的新增-------------------------------->
        <el-form ref="valueAdd" :model="valueAdd" label-width="80px" v-if="showValueAddForm">
          <el-form-item label="属性值" prop="value">
            <el-input v-model="valueAdd.value" size="small" style="width: 300px" clearable></el-input>
          </el-form-item>
          <el-form-item label="值名称" prop="valueName">
            <el-input v-model="valueAdd.valueName" size="small" style="width: 300px" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="showValueAddForm=false">取 消</el-button>
            <el-button type="primary" @click="addValue">添 加</el-button>
          </el-form-item>
        </el-form>

        <!----------------------------属性值的修改-------------------------------->
        <el-form ref="valueUpdate" :model="valueUpdate" label-width="80px" v-if="showValueUpdateForm">
          <el-form-item label="属性值" prop="value">
            <el-input v-model="valueUpdate.value" size="small" style="width: 300px" clearable></el-input>
          </el-form-item>
          <el-form-item label="值名称" prop="valueName">
            <el-input v-model="valueUpdate.valueName" size="small" style="width: 300px" clearable></el-input>
          </el-form-item>
          <el-form-item label="是否展示" prop="isDel">
            <el-radio-group v-model="valueUpdate.isDel">
              <el-radio :label="0">是</el-radio>
              <el-radio :label="1">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button @click="showValueUpdateForm=false">取 消</el-button>
            <el-button type="primary" @click="updateValue">修 改</el-button>
          </el-form-item>
        </el-form>

      </el-dialog>

    </div>
  </div>
</template>

<script>
  export default {
    name: "Attr",
    data() {
      return {
        /*---------属性---------*/
        query: {
          name: "",
          size: 2,
          current: 1
        },
        tableData: [],
        pageSizes: [2, 5, 10, 15],
        pageTotal: 0,
        dialogAttrAdd: false,
        dialogAttrUpdate: false,
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
        /*---------分类----------*/
        typeData: [],
        typeName: "",
        types: [
          /*{"id": 10, name: "分类/电子产品/手机"},
          {"id": 11, name: "分类/电子产品/音响"},
          {"id": 12, name: "分类/服饰/外套"},
          {"id": 13, name: "分类/服饰/下装"},
          {"id": 16, name: "分类/电子产品/摄像机"},
          {"id": 17, name: "分类/电子产品/电脑"}*/
        ],
        /*---------属性值---------*/
        search: {
          size: 3,
          current: 1
        },
        valueTitle: "",
        valueData: [],
        showValueTable: false,
        showValueAddForm: false,
        pageSizess: [3, 5, 7, 9],
        pageTotals: 0,
        valueAdd: {
          attrId: "",
          value: "",
          valueName: "",
        },
        showValueUpdateForm: false,
        valueUpdate: {
          value: "",
          valueName: "",
          isDel: ""
        }
      }
    },
    methods: {
      /*-----------------------------------------------属性值的函数----------------------------------------------*/
      //属性值列表弹框
      showValueTableDiv(row) {
        for (let i = 0; i < this.typeData.length; i++) {
          if (row.typeId === this.typeData[i].id) {
            var typeName = this.typeData[i].name;
          }
        }
        this.valueAdd.attrId = row.id;
        this.showValueTable = true;
        this.valueTitle = typeName + row.nameCH + "的属性值维护";
        this.getValueData(row.id);
      },
      //属性值分页查询
      getValueData(id) {
        this.$axios.get("http://localhost:8080/api/attrValue/getData?current=" + this.search.current + "&size=" + this.search.size + "&attrId=" + id).then(res => {
          this.valueData = res.data.data.list;
          this.pageTotals = res.data.data.count;
        })
      },
      //是否展示
      handleValue(row) {
        let del = row.isDel;
        let params = {
          id: row.id,
          isDel: del
        }
        this.$axios.post("http://localhost:8080/api/attrValue/delete", this.$qs.stringify(params)).then(res => {
          this.getValueData(this.valueAdd.attrId);
        })
      },
      //新增弹框
      addAttrValue() {
        this.showValueAddForm = true;
      },
      //新增
      addValue() {
        this.$axios.post("http://localhost:8080/api/attrValue/add", this.$qs.stringify(this.valueAdd)).then(res => {
          this.showValueAddForm = false;
          this.getValueData(this.valueAdd.attrId);
        })
      },
      //修改弹框回显
      updateAttrValue(row) {
        this.showValueUpdateForm = true;
        this.$axios.get("http://localhost:8080/api/attrValue/getDataById?id=" + row.id).then(res => {
          this.valueUpdate = res.data.data;
        })
      },
      //修改
      updateValue() {
        this.$axios.post("http://localhost:8080/api/attrValue/update", this.$qs.stringify(this.valueUpdate)).then(res => {
          this.showValueUpdateForm = false;
          this.getValueData(this.valueAdd.attrId);
        })
      },
      //上下页改变
      handlePage(val) {
        this.$set(this.search, 'current', val);
        this.getValueData();
      },
      //条数显示
      handleSize(val) {
        this.$set(this.search, 'current', 1);
        this.$set(this.search, 'size', val);
        this.getValueData();
      },

      /*-----------------------------------------------属性的函数----------------------------------------------------*/

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
          //遍历所有的节点数据     找到子节点
          this.getChildrenType();
          //遍历所有子节点
          for (let i = 0; i < this.types.length; i++) {
            this.typeName = "";
            //拼接name属性
            this.chandleName(this.types[i]);
            //翻转name属性
            this.types[i].name = this.typeName.split("/").reverse().join("/").substr(0,this.typeName.length-1);
          }
        })
      },
      //拼接name属性
      chandleName(node) {
        debugger
        if (node.pId != 0) {
          this.typeName += "/" + node.name;
          for (let i = 0; i < this.typeData.length; i++) {
            if (node.pid == this.typeData[i].id) {
              this.chandleName(this.typeData[i]);
              break;
            }
          }
        } else {
          this.typeName += node.name;
        }
      },
      //遍历所有的节点数据
      getChildrenType() {
        for (let i = 0; i < this.typeData.length; i++) {
          let node = this.typeData[i];
          this.isChildrenNode(node);
        }
      },
      //判断是否有pId指向  找到子节点
      isChildrenNode(node) {
        let rs = true;
        for (let i = 0; i < this.typeData.length; i++) {
          if (node.id == this.typeData[i].pid) {
            rs = false;
            break;
          }
        }
        if (rs == true) {
          //存入子节点
          this.types.push(node);
        }
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
    }
  }
</script>

<style scoped>

</style>
