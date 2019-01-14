package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.ExcelService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@RestController
@RequestMapping("/goodsExcel")
public class GoodsExcelController {
    @Reference
    private ExcelService excelService;

    @RequestMapping("/excelGoods")
    public Result findAll() throws Exception {

        try {
            List<Goods> goodsList = excelService.findAll();

            List<HashMap<String, Object>> listMap = new ArrayList<>();
            for (Goods goods : goodsList) {


                HashMap<String, Object> dataMap1 = new HashMap<>();

                dataMap1.put("Id", goods.getId());
                dataMap1.put("SellerId", goods.getSellerId());
                dataMap1.put("GoodsName", goods.getGoodsName());
                dataMap1.put("DefaultItemId", goods.getDefaultItemId());
                dataMap1.put("AuditStatus", goods.getAuditStatus());
                dataMap1.put("IsMarketable", goods.getIsMarketable());
                dataMap1.put("BrandId", goods.getBrandId());
                dataMap1.put("Caption", goods.getCaption());
                dataMap1.put("Category1Id", goods.getCategory1Id());
                dataMap1.put("Category2Id", goods.getCategory2Id());
                dataMap1.put("Category3Id", goods.getCategory3Id());
                dataMap1.put("SmallPic", goods.getSmallPic());
                dataMap1.put("Price", goods.getPrice());
                dataMap1.put("TypeTemplateId", goods.getTypeTemplateId());
                dataMap1.put("IsEnableSpec", goods.getIsEnableSpec());
                dataMap1.put("IsDelete", goods.getIsDelete());
                listMap.add(dataMap1);

            }


            String title = "商品Excel表";
            String[] rowsName = new String[] { "Id", "SellerId", "GoodsName", "DefaultItemId", "AuditStatus", "IsMarketable", "BrandId", "Caption", "Category1Id",
                    "Category2Id", "Category3Id", "SmallPic", "Price", "TypeTemplateId", "IsEnableSpec","IsDelete"};
            List<Object[]> dataList = new ArrayList <Object[]>();
            Object[] objs = null;
            for (int i = 0; i < listMap.size(); i++) {
                HashMap<String, Object> data = listMap.get(i);// 获取5次
                objs = new Object[rowsName.length];
                objs[0] = data.get("Id");
                objs[1] = data.get("SellerId");
                objs[2] = data.get("GoodsName");
                objs[3] = data.get("DefaultItemId");
                objs[4] = data.get("AuditStatus");
                objs[5] = data.get("IsMarketable");
                objs[6] = data.get("BrandId");
                objs[7] = data.get("Caption");
                objs[8] = data.get("Category1Id");
                objs[9] = data.get("Category2Id");
                objs[10] = data.get("Category3Id");
                objs[11] = data.get("SmallPic");
                objs[12] = data.get("Price");
                objs[13] = data.get("TypeTemplateId");
                objs[14] = data.get("IsEnableSpec");
                objs[15] = data.get("IsDelete");
                dataList.add(objs);
            }
            ExportExcel ex = new ExportExcel(title, rowsName, dataList);
            ex.export();
            return new Result(true, "导出商品表成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "导出商品表失败");

        }
    }

}
