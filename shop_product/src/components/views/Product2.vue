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
      <el-form :model="productForm2" label-width="100px" style="width: 700px;margin: 0 auto;" size="small">
        <el-form-item label="商品分类" prop="typeId">
          <!--  改变 获取属性数据  -->
          <el-select v-model="productForm2.typeId" clearable placeholder="请选择" @change="getAttrData">
            <el-option v-for="b in types" :key="b.id" :label="b.name" :value="b.id"></el-option>
          </el-select>
        </el-form-item>
        <!-------------------------------------------------商品规格---------------------------------------------------------->
        <el-form-item v-if="SKUData.length>0" label="商品规格" prop="name">
          <el-form-item v-for="a in SKUData" :key="a.id" :label="a.nameCH+' ：'">
            <el-checkbox-group v-model="a.ckValues" v-if="a.type==2" @change="skuChange">
              <el-checkbox v-for="b in a.values" :key="b.id" :label="b.valueName" :value="b.id"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form-item>
        <el-table v-if="tableShow"
                  :data="tableData"
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
              <el-input v-model="scope.row.stockss"></el-input>
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
        typeName: "",
        types: [],//分类数据
        /*--------------------------------------------修改商品属性数据------------------------------------------------*/
        attrShow:false,
        productForm2: {},//商品属性数据form
        SKUData: [], //sku属性数据
        attData: [], //非sku的属性数据
        tableShow: false, //SKU动态表格是否显示
        cols: [],//表动态列头
        tableData: [], //SKU的动态表格数据
        stockss:"",
        prices:"",
      }
    },
    methods: {
      /*-------------------------------------------属性数据--------------------------------------------------*/
      //回显属性数据
      toAttrForm(typeId,pid){
        this.attrShow = true;
        //回显分类id
        this.productForm2.typeId = typeId;
        //回显属性
        this.getAttrData2(typeId,pid);
        this.skuChange();
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
      //根据分类 typeId 回显 属性 和 属性值
      getAttrData2(typeId,pid) {
        //回显属性数据
        this.$axios.get("http://localhost:8080/api/product/queryProdectAttrByPid?pid="+pid).then(res=>{
          debugger;
          let datas = res.data.data;
          console.log(datas);
          this.SKUData = [];
          this.attData = [];
          //查询属性数据
          this.$axios.get("http://localhost:8080/api/attr/getDataByTypeId?typeId=" + typeId).then(res => {
            //所有属性
            let attrDatas = res.data.data;
            //判断是否有数据
            if (attrDatas.length > 0) {
              //遍历属性
              for (let i = 0; i < attrDatas.length; i++) {
                //判断是SKU属性
                if (attrDatas[i].isSKU == 1) {
                  //判断不是输入框
                  if (attrDatas[i].type != 3) {
                    //回显
                    if (attrDatas[i].type == 2){
                      if (this.getValeu(attrDatas[i].name,datas) == ""){
                        attrDatas[i].ckValues = [];
                      }else{
                        attrDatas[i].ckValues = this.getValeu(attrDatas[i].name,datas);
                      }
                    }else {
                      attrDatas[i].ckValues = this.getValeu(attrDatas[i].name,datas);
                    }
                    //查询属性对应的属性值
                    this.$axios.get("http://localhost:8080/api/attrValue/getData?current="+1+"&size="+1000+"&attrId="+attrDatas[i].id).then(res => {
                      attrDatas[i].values = res.data.data.list;
                      this.attData.push(attrDatas[i]);
                    })
                  } else {
                    attrDatas[i].ckValues = this.getValeu(attrDatas[i].name,datas);
                    this.attData.push(attrDatas[i]);
                  }
                } else {
                  //判断不是输入框
                  if (attrDatas[i].type != 3) {
                    //回显
                    if (attrDatas[i].type == 2){
                      if (this.getValeu(attrDatas[i].name,datas) == ""){
                        attrDatas[i].ckValues = [];
                      }else{
                        attrDatas[i].ckValues = this.getValeu(attrDatas[i].name,datas);
                      }
                    }else {
                      attrDatas[i].ckValues = this.getValeu(attrDatas[i].name,datas);
                    }
                    //查询属性对应的属性值
                    this.$axios.get("http://localhost:8080/api/attrValue/getData?current=" + 1 + "&size=" + 1000 + "&attrId=" + attrDatas[i].id).then(res => {
                      attrDatas[i].values = res.data.data.list;
                      attrDatas[i].ckValues = this.getValeu(attrDatas[i].name,datas);
                      this.SKUData.push(attrDatas[i]);
                    })
                  } else {
                    attrDatas[i].ckValues = [];
                    this.SKUData.push(attrDatas[i]);
                  }
                }
              }
            } else {
              this.SKUData = [];
              this.attData = [];
            }
          })
        })
      },
      //根据分类 typeId 的改变，查询 属性 和 属性值
      getAttrData(typeId) {
        this.SKUData = [];
        this.attData = [];
        //查询属性数据
        this.$axios.get("http://localhost:8080/api/attr/getDataByTypeId?typeId=" + typeId).then(res => {
          //所有属性
          let attrDatas = res.data.data;
          //判断是否有数据
          if (attrDatas.length > 0) {
            //遍历属性
            for (let i = 0; i < attrDatas.length; i++) {
              //判断是SKU属性
              if (attrDatas[i].isSKU == 1) {
                //判断不是输入框
                if (attrDatas[i].type != 3) {
                  attrDatas[i].ckValues = [];
                  //查询属性对应的属性值
                  this.$axios.get("http://localhost:8080/api/attrValue/getData?current=" + 1 + "&size=" + 1000 + "&attrId=" + attrDatas[i].id).then(res => {
                    attrDatas[i].values = res.data.data.list;
                    this.attData.push(attrDatas[i]);
                  })
                } else {
                  this.attData.push(attrDatas[i]);
                }
              } else {
                //判断不是输入框
                if (attrDatas[i].type != 3) {
                  //查询属性对应的属性值
                  this.$axios.get("http://localhost:8080/api/attrValue/getData?current=" + 1 + "&size=" + 1000 + "&attrId=" + attrDatas[i].id).then(res => {
                    attrDatas[i].values = res.data.data.list;
                    attrDatas[i].ckValues = [];
                    this.SKUData.push(attrDatas[i]);
                  })
                } else {
                  attrDatas[i].ckValues = [];
                  this.SKUData.push(attrDatas[i]);
                }
              }
            }
          } else {
            this.SKUData = [];
            this.attData = [];
          }
        })
      },

      getValeu:function(key,data){
        //data 回显数据   [{},{}]
        /*key
        *
        *  [
              {
                  "id": 14,
                  "proId": 12,
                  "attrData": "{\"memsize\":\"16G\",\"pricess\":\"111\",\"color\":\"绿色\",\"netType\":\"联通\",\"storcks\":\"111\"}",
                  "storcks": null,
                  "price": null
              },
              {
                  "id": 15,
                  "proId": 12,
                  "attrData": "{\"memsize\":\"16G\",\"pricess\":\"22\",\"color\":\"红色\",\"netType\":\"联通\",\"storcks\":\"222\"}",
                  "storcks": null,
                  "price": null
              }
          ]
        * */
        let  arrValue=[];
        //debugger;
        //遍历当前商品对应的所有的属性
        for (let i = 0; i <data.length ; i++) {
          //得到当前属性数据的一个 将字符串转为json
          let  objData=JSON.parse(data[i].attrData);
          // 判断当前的属性数据 是否为想要的属性值
          if(objData[key]!=null){ //找到对应的值了
            //判断当前属性是否为sku
            if(data[i].stocks!=null){
              if(arrValue.indexOf(objData[key])==-1){
                arrValue.push(objData[key]);
              }
            }else{
              return objData[key];
            }
          }
        }
        return arrValue;
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
            this.types[i].name = this.typeName.split("/").reverse().join("/").substr(0, this.typeName.length - 1);
          }
        })
      },
      //拼接name属性
      chandleName(node) {
        if (node.pId != 0) {
          if (node.id != 1){
            this.typeName += "/" + node.name;
            for (let i = 0; i < this.typeData.length; i++) {
              if (node.pid == this.typeData[i].id) {
                this.chandleName(this.typeData[i]);
                break;
              }
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
      classType(row, column) {
        for (let i = 0; i < this.typeData.length; i++) {
          if (row.typeId == this.typeData[i].id) {
            return this.typeData[i].name;
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
