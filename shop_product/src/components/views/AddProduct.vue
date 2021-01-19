<template>
  <el-card class="form-container" shadow="never">
    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="填写商品信息"></el-step>
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
        <el-form-item label="商品简介" prop="productDecs">
          <el-input type="textarea" v-model="productForm.productDecs" size="small" style="width: 500px" clearable></el-input>
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

        <el-form-item v-if="SKUData.length>0" label="商品规格" prop="name">
          <el-form-item v-for="a in SKUData" :key="a.id" :label="a.nameCH+' ：'">

            <el-checkbox-group v-model="a.ckValues" v-if="a.type==2" @change="skuChange">
              <el-checkbox v-for="b in a.values" :key="b.id" :label="b.valueName" name="type"></el-checkbox>
            </el-checkbox-group>

          </el-form-item>
        </el-form-item>

        <el-table v-if="tableShow" style="width: 100%;margin-top: 20px" border>
          <el-table-column
            label="销售价格"
            width="80"
            align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.price"></el-input>
            </template>
          </el-table-column>
        </el-table>

        <el-form-item v-if="attData.length>0" label="商品参数" prop="name">
          <el-form-item v-for="a in  attData" :key="a.id" :label="a.nameCH+' ：'">
            <!--  0 下拉框     1 单选框      2  复选框   3  输入框  -->
            <el-select v-if="a.type==0" placeholder="请选择">
              <el-option v-for="b in a.values" :key="b.id" :label="b.valueName" value="b.id"></el-option>
            </el-select>

            <el-radio-group v-if="a.type==1">
              <el-radio v-for="b in a.values" :key="b.id" :label="b.valueName"></el-radio>
            </el-radio-group>

            <el-checkbox-group v-if="a.type==2">
              <el-checkbox v-for="b in a.values" :key="b.id" :label="b.valueName" name="type"></el-checkbox>
            </el-checkbox-group>

            <el-input v-if="a.type==3"></el-input>

          </el-form-item>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="medium" @click="active--">上一步，填写商品信息</el-button>
          <el-button type="primary" size="medium" @click="next">下一步，上传商品图片</el-button>
        </el-form-item>
      </el-form>
    </div>
<!-------------------------------------------------第三步---------------------------------------------------------->


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
        productForm: {
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
          name: [
            { required: true, message: '请输入商品名称', trigger: 'blur' },
            { min: 3, max: 5, message: '长度在 2 到 8 个字符', trigger: 'blur' }
          ],
          title: [
            { required: true, message: '请输入商品标题', trigger: 'blur' },
          ],
          brandId: [
            { required: true, message: '请选择商品品牌', trigger: 'change' }
          ],
        },
        brandDatas: [],
        /*-------------第二步相关的数据-------------*/
        productForm2: {},
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
        SKUData: [], //sku属性数据
        attData: [], //非sku的属性数据
        tableShow: false,

      }
    },
    methods: {

      /*-----------------------------------------------商品属性---------------------------------------------------*/

      //SKU属性改变事件
      skuChange(){
        let flag = true;
        for (let i = 0; i < this.SKUData.length; i++) {
          if (this.SKUData[i].ckValues.length == 0){
            flag = false;
            break;
          }else {
            flag = true;
          }
        }
        this.tableShow = flag;
      },

      //根据分类的改变，查询 属性 和 属性值
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
        this.$refs["productForm"].validate((valid) => {
          if (valid){
            this.getTypeData();
            if (this.active++ > 1) this.active = 0;
          }
        })
      },
    },
    mounted() {
      //查询品牌
      this.getBrandData();
    }
  }
</script>

<style scoped>

</style>
