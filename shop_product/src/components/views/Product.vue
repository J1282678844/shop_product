<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 商品列表
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-input v-model="query.name" placeholder="商品名称" class="handle-input mr10"></el-input>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
      </div>
      <el-table
        :data="tableData"
        border
        class="table"
        ref="multipleTable"
        header-cell-class-name="table-header">
        <el-table-column type="selection" width="50" align="center"></el-table-column>
        <el-table-column prop="id" label="编号" width="50" align="center"></el-table-column>
        <el-table-column prop="name" label="商品名称" width="80" align="center"></el-table-column>
        <el-table-column prop="title" label="商品标题" width="120" align="center"></el-table-column>
        <el-table-column prop="brandId" label="商品品牌" width="80" align="center" :formatter="classBrand"></el-table-column>
        <el-table-column prop="typeId" label="商品分类" width="90" align="center" :formatter="classType"></el-table-column>
        <el-table-column prop="productDecs" label="商品描述" width="150" align="center"></el-table-column>
        <el-table-column prop="price" label="商品价格" width="80" align="center"></el-table-column>
        <el-table-column prop="stocks" label="商品库存" width="80" align="center"></el-table-column>
        <el-table-column prop="sort" label="排序" width="50" align="center"></el-table-column>
        <el-table-column label="商品logo" width="80" align="center">
          <template slot-scope="scope">
            <el-image
              class="table-td-thumb"
              :src="scope.row.imgpath"
              :preview-src-list="[scope.row.imgpath]"
            ></el-image>
          </template>
        </el-table-column>
        <el-table-column label="是否显示" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              @change="handleDelete(scope.row)"
              :active-value="0"
              :inactive-value="1"
              v-model="scope.row.isDel">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="mini"
              @click="toProductForm(scope.row)">商品编辑
            </el-button>
            <el-button
              type="primary"
              size="mini"
              @click="toAttrForm(scope.row.typeId,scope.row.id)">属性编辑
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
    <!------------------------------------------修改商品基本数据弹出框---------------------------------------------->
    <el-dialog title="修改信息" width="40%" :visible.sync="dialogProductUpdate">
      <el-form ref="productUpdate" :model="productUpdate" label-width="120px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="productUpdate.name" size="small" style="width: 300px" clearable></el-input>
        </el-form-item>
        <el-form-item label="商品标题" prop="title">
          <el-input v-model="productUpdate.title" size="small" style="width: 300px" clearable></el-input>
        </el-form-item>
        <el-form-item label="商品品牌" prop="brandId">
          <el-select v-model="productUpdate.brandId" clearable placeholder="请选择">
            <el-option v-for="b in brandDatas" :key="b.id" :label="b.name" :value="b.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品描述" prop="productDecs">
          <el-input
            type="textarea"
            :rows="2"
            maxlength="200"
            show-word-limit
            placeholder="请输入商品描述"
            v-model="productUpdate.productDecs"
            size="small" style="width: 350px" clearable>
          </el-input>
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input-number v-model="productUpdate.price" :precision="2" :step="0.1"></el-input-number>
        </el-form-item>
        <el-form-item label="商品库存" prop="stocks">
          <template>
            <el-input-number v-model="productUpdate.stocks" :step="10"></el-input-number>
          </template>
        </el-form-item>
        <el-form-item label="商品排序" prop="sort">
          <template>
            <el-input-number v-model="productUpdate.sort" :step="1"></el-input-number>
          </template>
        </el-form-item>
        <el-form-item label="是否展示" prop="isDel">
          <el-radio-group v-model="productUpdate.isDel">
            <el-radio :label="0">是</el-radio>
            <el-radio :label="1">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="商品LOGO" prop="imgpath">
          <single-upload v-model="productUpdate.imgpath"></single-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="dialogProductUpdate=false">取 消</el-button>
            <el-button type="primary" @click="updatePinpai">修 改</el-button>
        </span>
    </el-dialog>
    <!------------------------------------------修改商品属性数据弹出框---------------------------------------------->
    <el-dialog title="属性信息" :visible.sync="attrShow">
      <el-form :model="productForm" label-width="100px" style="width: 700px;margin: 0 auto;" size="small">
        <el-form-item label="商品分类" prop="typeId">
          <!--  改变 获取属性数据  -->
          <el-select v-model="productForm.typeId" clearable placeholder="请选择">
            <el-option v-for="b in types" :key="b.id" :label="b.name" :value="b.id"></el-option>
          </el-select>
        </el-form-item>
        <!-------------------------------------------------商品规格---------------------------------------------------------->
        <el-form-item v-if="SKUData.length>0" label="商品规格" prop="name">
          <el-form-item v-for="a in SKUData" :key="a.id" :label="a.nameCH+' ：'">
            <el-checkbox-group v-model="a.ckValues" @change="skuChange">
              <el-checkbox v-for="b in a.values" :key="b.id" :label="b.valueName" :value="b.id"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form-item>
        <el-table v-if="tableShow"
                  :data="tableSkuData"
                  style="width: 100%;margin-top: 20px"
                  border>
          <el-table-column
            v-for="c in cols" :key="c.id" :label="c.nameCH" :prop="c.name" align="center">
          </el-table-column>
          <el-table-column
            label="销售价格"
            width="100"
            align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.prices"></el-input>
            </template>
          </el-table-column>
          <el-table-column
            label="商品库存"
            width="100"
            align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.stocks"></el-input>
            </template>
          </el-table-column>
        </el-table>
        <!-------------------------------------------------商品参数---------------------------------------------------------->
        <el-form-item v-if="attData.length>0" label="商品参数" prop="name">
          <el-form-item v-for="a in  attData" :key="a.id" :label="a.nameCH+' ：'">
            <!--  0 下拉框     1 单选框      2  复选框   3  输入框  -->
            <el-select v-if="a.type==0" placeholder="请选择" v-model="a.ckValues">
              <el-option v-for="b in a.values" :key="b.id" :label="b.valueName" :value="b.id"></el-option>
            </el-select>
            <el-radio-group v-if="a.type==1" v-model="a.ckValues">
              <el-radio v-for="b in a.values" :key="b.id" :label="b.valueName"></el-radio>
            </el-radio-group>
            <el-checkbox-group v-if="a.type==2" v-model="a.checks">
              <el-checkbox v-for="b in a.values" :key="b.id" :label="b.valueName"></el-checkbox>
            </el-checkbox-group>
            <el-input v-if="a.type==3" v-model="a.ckValues" size="small" style="width: 500px" clearable></el-input>
          </el-form-item>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="attrShow = false">取 消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import SingleUpload from "../SingleUpload";

  export default {
    name: "Product",
    components: {SingleUpload},
    data() {
      return {
        /*--------------------------------------------商品表格------------------------------------------------*/
        query: {
          name: "",
          size: 5,
          current: 1
        },
        tableData: [],
        pageSizes: [5, 10, 15, 20],
        pageTotal: 0,
        /*--------------------------------------------修改商品基本数据------------------------------------------------*/
        productUpdate: {//商品基本数据form
          name: "",
          title: "",
          brandId: "",
          productDecs: "",
          price: 0,
          stocks: 0,
          sort: 0,
          imgpath: "",
          isDel: 1
        },
        dialogProductUpdate: false,
        brandDatas: [],
        /*-------------------------------------------------分类----------------------------------------------------*/
        typeData: [],
        types: [],//分类数据
        /*--------------------------------------------修改商品属性数据------------------------------------------------*/
        attrShow:false,
        productForm: {
          typeId:0
        },//商品属性数据form
        SKUData: [], //sku属性数据
        attData: [], //非sku的属性数据
        tableShow: false, //SKU动态表格是否显示
        cols: [],//表动态列头
        tableSkuData: [], //SKU的动态表格数据
      }
    },
    methods: {
      /*-------------------------------------------属性数据--------------------------------------------------*/
      //回显属性数据
      async toAttrForm(typeId, pid) {
        this.attrShow = true;
        //显示分类内容
        this.huixianType();
        //回显分类id
        this.productForm.typeId = typeId;
        //显示sku 和attr 内容
        await this.showAttrData();
        //回显sku的选项{"memsize":["32g"],"color":['红色','绿色'],"netType":'联通'}
        await this.huixianSKU(pid);
        //回显 SKU table
        this.showTable();
        this.$forceUpdate();
      },
      //SKU属性改变事件
      skuChange() {
        //声明笛卡尔积的 参数
        let dikaParams = [];
        //判断是否生成笛卡尔积
        let flag = true;
        //遍历所有SKU
        for (let i = 0; i < this.SKUData.length; i++) {
          //添加动态表头名称
          this.cols.push({"id": this.SKUData.id, "nameCH": this.SKUData[i].nameCH, "name": this.SKUData[i].name});
          //将选中的属性  放入  笛卡尔积数组
          dikaParams.push(this.SKUData[i].ckValues);
          //判断是否选中SKU属性 =0 未选中
          if (this.SKUData[i].ckValues.length == 0) {
            //不生成笛卡尔积
            flag = false;
            break;
          }
        }
        if (flag == true) {
          //将笛卡尔积数组  放入  笛卡尔积     颜色  [红色,绿色]    尺寸 [x,xl]
          let dikaerji = this.calcDescartes(dikaParams);
          //遍历笛卡尔积    [[红色,x],[红色,xl],[绿色,x],[绿色,xl]]
          for (let i = 0; i < dikaerji.length; i++) {
            //将数据json化
            let jsonTable = {};
            if (typeof dikaerji[i] == "object"){
              //遍历笛卡尔积的 数据
              for (let j = 0; j < dikaerji[i].length; j++) {
                //获取列头的  key
                let key = this.cols[j].name;
                //设置数据的key      {{color: "红色", size: "x"},{color: "红色", size: "xl"}}
                jsonTable[key] = dikaerji[i][j];
              }
            }else {
              //获取列头的  key
              let key = this.cols[0].name;
              //设置数据的key      {{color: "红色", size: "x"},{color: "红色", size: "xl"}}
              jsonTable[key] = dikaerji[i];
            }

            //console.log(jsonTable)
            //将数据  放到  表格中
            this.tableData.push(jsonTable);
          }
        }
        this.tableShow = flag;
      },
      //笛卡尔积
      calcDescartes: function (array) {
        if (array.length < 2) return array[0] || [];
        return [].reduce.call(array, function (col, set) {
          var res = [];
          col.forEach(function (c) {
            set.forEach(function (s) {
              var t = [].concat(Array.isArray(c) ? c : [c]);
              t.push(s);
              res.push(t);
            })
          });
          return res;
        });
      },
      //回显 SKU table
      showTable: function () {
        //判断是否需要回显table
        if (this.SKUData.length > 0) {
          this.tableShow = true;
          //设置动态表头 //动态表头[ {id:skudata.id,name:skudata.name,nameCH:skudata.nameCH}]
          for (let i = 0; i < this.SKUData.length; i++) {
            this.cols.push({
              "id": this.SKUData[i].id,
              "name": this.SKUData[i].name,
              "nameCH": this.SKUData[i].nameCH
            });
          }
        }
      },
      huixianSKU: async function (pid) {
        //根据商品id查询对应的sku选中的值{"code":200,"message":"success",
        // "data":{"skudata":{"memsize":["32G"],"pricess":["222",111],"color":["绿色","红色"],"netType":["联通"],"storcks":["222",111]},"attrdata":{"peoples":["中年","老年","幼年"],"factory":"苹果尝试","system":"苹果系统","cpu":"骁龙1000"}}}
        let datas = await this.$axios.get("http://localhost:8080/api/product/querySKUckvalues?pid=" + pid);
        //处理sku
        let data = datas.data.data.skudata;
        //遍历所有的sku属性
        for (let i = 0; i < this.SKUData.length; i++) {
          //得到一个sku属性
          this.SKUData[i].ckValues = data[this.SKUData[i].name];
        }
        //处理attr
        let data2 = datas.data.data.attrdata;
        //遍历所有的sku属性
        for (let i = 0; i < this.attData.length; i++) {
          //得到一个sku属性
          this.attData[i].ckValues = data2[this.attData[i].name];
        }
        //table赋值
        this.tableSkuData = datas.data.data.tableData;
      },
      /* 获取 sku的数据和attr的数据  */
      showAttrData: async function () {
        //获取此分类下的sku数据和attr 数据
        let res = await this.$axios.get("http://localhost:8080/api/attr/queryAttrByTypeId?typeId=" + this.productForm.typeId);
        //处理sku的
        //得到所有的sku数据
        this.SKUData = res.data.data.skuDatas;
        //商品规格 设置初始化值 添加一个属性 ckValues  []  [{"id": 3, "name": "color", "nameCH": "颜色", "values": [{"id": 5, "value": "red", "valueCH": "红色",}]
        for (let i = 0; i < this.SKUData.length; i++) {
          this.SKUData[i].ckValues = [];
        }
        //处理attr数据
        this.attData = res.data.data.attrDatas;
        //商品规格  复选框 设置初始化值 添加一个属性 ckValues  []  [{"id": 3, "name": "color", "nameCH": "颜色", "values": [{"id": 5, "value": "red", "valueCH": "红色",}]
        for (let i = 0; i < this.attData.length; i++) {
          if (this.attData[i].type == 2) {
            this.attData[i].ckValues = [];
          }
        }
      },
      //查询分类数据
      huixianType:async function() {
        let res = await this.$axios.get("http://localhost:8080/api/type/getData")
        this.getChildrenType(res.data.data);
      },
      //遍历所有的节点数据
      getChildrenType(datas) {
        //判断当前节点是否为子节点
        for (let i = 0; i < datas.length; i++) {
          //得到一个分类数据
          let node = datas[i];
          let nodeFlag = true; //默认当前节点是子节点
          //遍历所有的分类数据
          for (let j = 0; j < datas.length; j++) {
            //判断当前节点是否为子节点       当前节点数据  在所有的数据中 是否有pid指向当前id的
            if (node.id == datas[j].pid) {
              nodeFlag = false;
              break;
            }
          }
          //判断是否为子节点
          if (nodeFlag == true) {
            this.types.push(node);
          }
        }
      },

      /*-------------------------------------------基本数据--------------------------------------------------*/
      //分页查询基本数据
      getData() {
        this.$axios.get("http://localhost:8080/api/product/getData?current=" + this.query.current + "&size=" + this.query.size + "&name=" + this.query.name).then(res => {
          this.tableData = res.data.data.list;
          this.pageTotal = res.data.data.count;
        })
      },
      //条件查询基本数据
      handleSearch() {
        this.getData();
      },
      //回显基本数据
      toProductForm(row) {
        this.dialogProductUpdate = true;
        this.$axios.get("http://localhost:8080/api/product/getDataById?id=" + row.id).then(res => {
          this.productUpdate = res.data.data;
        })
      },
      //修改基本数据
      updatePinpai() {
        this.$axios.post("http://localhost:8080/api/product/update", this.$qs.stringify(this.productUpdate)).then(res => {
          this.dialogProductUpdate = false;
          this.getData();
        })
      },
      //是否展示开关
      handleDelete(row) {
        let del = row.isDel;
        let params = {
          id: row.id,
          isDel: del
        }
        this.$axios.post("http://localhost:8080/api/product/delete", this.$qs.stringify(params)).then(res => {
          this.getData();
        })
      },
      //查询品牌数据
      getBrandData() {
        this.$axios.get("http://localhost:8080/api/brand/getData?current=" + 1 + "&size=" + 1000).then(res => {
          this.brandDatas = res.data.data.list;
        })
      },
      //初始化属性名称
      classBrand(row,column){
        for (let i = 0; i < this.brandDatas.length; i++) {
          if (row.brandId === this.brandDatas[i].id) {
            return this.brandDatas[i].name;
          }
        }
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
    },
    mounted() {
      this.getData();
      this.getBrandData();
      this.getTypeData();
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
