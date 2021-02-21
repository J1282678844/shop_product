<template>
  <el-card class="form-container" shadow="never">
    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="填写基本信息"></el-step>
      <el-step title="填写商品属性"></el-step>
      <el-step title="上传商品图片"></el-step>
    </el-steps>
    <!-------------------------------------------------第一步---------------------------------------------------------->
    <div style="margin-top: 50px" v-if="active==0">
      <el-form :model="productForm" :rules="rules" ref="productForm"
               label-width="100px" style="width: 700px;margin: 0 auto;" size="small">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="productForm.name" size="small" style="width: 500px" clearable></el-input>
        </el-form-item>
        <el-form-item label="商品标题" prop="title">
          <el-input v-model="productForm.title" size="small" style="width: 500px" clearable></el-input>
        </el-form-item>
        <el-form-item label="商品品牌" prop="brandId">
          <el-select v-model="productForm.brandId" clearable placeholder="请选择">
            <el-option v-for="b in brandDatas" :key="b.id" :label="b.name" :value="b.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品描述" prop="productDecs">
          <el-input type="textarea" v-model="productForm.productDecs" size="small" style="width: 500px"
                    clearable></el-input>
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input-number v-model="productForm.price" :precision="2" :step="0.1"></el-input-number>
        </el-form-item>
        <el-form-item label="商品库存" prop="stocks">
          <template>
            <el-input-number v-model="productForm.stocks" :step="10"></el-input-number>
          </template>
        </el-form-item>
        <el-form-item label="商品排序" prop="sort">
          <template>
            <el-input-number v-model="productForm.sort" :step="1"></el-input-number>
          </template>
        </el-form-item>
        <!-- 图片插件  -->
        <el-form-item label="商品主图" prop="imgpath">
          <single-upload v-model="productForm.imgpath"></single-upload>
        </el-form-item>
        <el-form-item>
          <el-button>重置</el-button>
          <el-button type="primary" size="medium" @click="next">下一步，填写商品属性</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-------------------------------------------------第二步---------------------------------------------------------->
    <div style="margin-top: 50px" v-if="active==1">
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

        <el-form-item>
          <el-button type="primary" size="medium" @click="active--">上一步，填写基本信息</el-button>
          <el-button type="primary" size="medium" @click="next">下一步，上传商品图片</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-------------------------------------------------第三步---------------------------------------------------------->
    <div style="margin-top: 50px" v-if="active==2">
      <el-form label-width="100px" style="width: 700px;margin: 0 auto;" size="small">
        <el-form-item>
          <el-button type="primary" size="medium" @click="active--">上一步，填写商品属性</el-button>
          <el-button type="primary" size="medium" @click="toSubmit">提交商品信息</el-button>
        </el-form-item>
      </el-form>
    </div>

  </el-card>
</template>

<script>
  import SingleUpload from "../SingleUpload";

  export default {
    name: "ProductStep",
    components: {SingleUpload},
    data() {
      return {
        active: 0,
        /* ------------第一步相关的数据------------ */
        productForm: {//商品基本数据form
          name: "",
          title: "",
          brandId: "",
          productDecs: "",
          price: 0,
          stocks: 0,
          sort: 0,
          imgpath: ""
        },
        rules: {
          /*name: [
            { required: true, message: '请输入商品名称', trigger: 'blur' },
            { min: 3, max: 5, message: '长度在 2 到 8 个字符', trigger: 'blur' }
          ],
          title: [
            { required: true, message: '请输入商品标题', trigger: 'blur' },
          ],
          brandId: [
            { required: true, message: '请选择商品品牌', trigger: 'change' }
          ],*/
        },
        brandDatas: [],
        /*-------------第二步相关的数据-------------*/
        productForm2: {},//商品属性数据form
        typeData: [],//接口返回的分类数据
        typeName: "",//拼接分类的名称
        types: [
          /*{"id": 10, name: "分类/电子产品/手机"},
          {"id": 11, name: "分类/电子产品/音响"},
          {"id": 12, name: "分类/服饰/外套"},
          {"id": 13, name: "分类/服饰/下装"},
          {"id": 16, name: "分类/电子产品/摄像机"},
          {"id": 17, name: "分类/电子产品/电脑"}*/
        ],//分类数据
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
      /*-----------------------------------------------提交商品---------------------------------------------------*/
      //提交商品信息
      toSubmit(){
        //将商品的分类id   加到 productForm
        this.productForm.typeId = this.productForm2.typeId;
        //console.log(this.productForm);
        //skus数据
        //console.log(this.tableData);
        //非sku数据
        //console.log(this.attData);
        //处理  非sku数据
        let attrs = [];
        for (let i = 0; i < this.attData.length; i++) {
          let attData = {};
          attData[this.attData[i].name] = this.attData[i].ckValues;
          attrs.push(attData);
        }
        this.productForm.attr = JSON.stringify(attrs);//将  JSON转为 JSON字符串  传后台
        this.productForm.sku = JSON.stringify(this.tableData);//将  JSON转为 JSON字符串  传后台
        //console.log(attrs);
        //新增
        this.$axios.post("http://localhost:8080/api/product/add",this.$qs.stringify(this.productForm)).then(res =>{
          this.$message.success("新增成功");
          this.$router.push({path:'/product'})
        })
      },
      /*-----------------------------------------------商品属性---------------------------------------------------*/
      //SKU属性改变事件
      skuChange() {
        this.cols = [];//清空表头
        this.tableData = [];//清空表格数据
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
      //根据分类 typeId 的改变，查询 属性 和 属性值
      getAttrData(typeId) {
        //关闭表格
        this.tableShow = false;
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
                  //查询属性对应的属性值
                  this.$axios.get("http://localhost:8080/api/attrValue/getData?current=" + 1 + "&size=" + 1000 + "&attrId=" + attrDatas[i].id).then(res => {
                    attrDatas[i].values = res.data.data.list;
                    attrDatas[i].checks = [];
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

      /*-----------------------------------------------商品信息---------------------------------------------------*/

      //查询品牌
      getBrandData() {
        this.$axios.get("http://localhost:8080/api/brand/getData?current=" + 1 + "&size=" + 1000).then(res => {
          this.brandDatas = res.data.data.list;
        })
      },

      /*  步骤条  下一页  */
      next() {
        //获取验证
        //this.$refs["productForm"].validate((valid) => {
          //if (valid) {
            this.getTypeData();
            if (this.active++ > 1) this.active = 0;
          //}
        //})
      },
    },
    mounted() {
      //查询品牌
      this.getBrandData();
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
