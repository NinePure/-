package cn.itcast.core.controller;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExportExcelorder {




        private String title;// 显示导出表的标题
        private String[] rowName;// 导出表的列名
        private List<Object[]> dataList = new ArrayList<Object[]>();

        public ExportExcelorder(String title, String[] rowName, List<Object[]> dataList) {
            super();
            this.title = title;
            this.rowName = rowName;
            this.dataList = dataList;
        }

        // 导出数据
        public void export() throws Exception {
            try {
                HSSFWorkbook workbook = new HSSFWorkbook();// 创建工作簿对象
                HSSFSheet sheet = workbook.createSheet(title);

                // 产生表格标题行，第一行
              HSSFRow rowm1 = sheet.createRow(0);
                HSSFCell cell1 = rowm1.createCell(0);
                rowm1.setHeight((short) (40 * 20));//设置第一行的高度

                // sheet样式定义[getcolumnTopStyle()/getStyle()均为自定义方法-在下面-可扩展]
                HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);
                HSSFCellStyle style = this.getStyle(workbook);

                // 定义所需要的列数
                int columnNum = rowName.length;
                HSSFRow rowRowName1 = sheet.createRow(0);
                HSSFRow rowRowName2 = sheet.createRow(1);
                rowRowName1.setHeight((short) (40 * 20));// 设置第一行高度


                // 将列头设置到sheet单元格中
                for (int n = 0; n < columnNum; n++) {
                    // 设置第一行的值
                    HSSFCell cellRowName1 = rowRowName1.createCell(n); // 创建列头对应个数的单元格
                    cellRowName1.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
                    HSSFRichTextString text1 = new HSSFRichTextString(rowName[n]);
                    cellRowName1.setCellValue(text1); // 设置列头单元格的值
                    cellRowName1.setCellStyle(columnTopStyle); // 设置列头单元格样式

                }


                // 将查询出的数据设置到sheet对应的单元格中
                for (int i = 0; i < dataList.size(); i++) {

                    Object[] obj = dataList.get(i);// 遍历每个对象
                    HSSFRow row = sheet.createRow(i + 1);// 创建所需的行数

                    row.setHeight((short) (40* 20)); // 设置第三行开始的单元格高度

                    for (int j = 0; j < obj.length; j++) {
                        HSSFCell cell = null; // 设置单元格的数据类型
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                        if (!"".equals(obj[j]) && obj[j] != null) {
                            cell.setCellValue(obj[j].toString()); // 设置单元格的值
                        }
                        cell.setCellStyle(style); // 设置单元格样式
                    }
                }

                // 让列宽随着导出的列长自动适应



          for (int colNum = 0; colNum < columnNum; colNum++) {
                 int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                 HSSFRow currentRow;
                 // 当前行未被使用过
                 if (sheet.getRow(rowNum) == null) {
                currentRow = sheet.createRow(rowNum);
                } else {
                currentRow = sheet.getRow(rowNum);
                }
               if (currentRow.getCell(colNum) != null) {
                HSSFCell currentCell = currentRow.getCell(colNum);

                if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING)
                { int length = 35;
                    //*currentCell.getStringCellValue().getBytes().length;*//*
                if (columnWidth < length)
                { columnWidth = length; }
                }
                }
                }
                if (colNum == 0) {
            sheet.setColumnWidth(colNum, (columnWidth - 2) * 128);
            } else {
            sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
            }

            }


                if (workbook != null) {
                    try {
                        FileOutputStream out = new FileOutputStream(
                                "e:/img/" +"订单Excel表"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString() + ".xls");
                        workbook.write(out);
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        /*
         * 列头单元格样式
         */
        public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

            // 设置字体
            HSSFFont font = workbook.createFont();
            // 设置字体大小
            font.setFontHeightInPoints((short) 9);
            // 字体加粗
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            // 设置字体名字
            font.setFontName("Times New Roman");
            // 设置样式;
            HSSFCellStyle style = workbook.createCellStyle();
            // 设置底边框;
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            // 设置底边框颜色;
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            // 设置左边框;
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            // 设置左边框颜色;
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            // 设置右边框;
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            // 设置右边框颜色;
            style.setRightBorderColor(HSSFColor.BLACK.index);
            // 设置顶边框;
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            // 设置顶边框颜色;
            style.setTopBorderColor(HSSFColor.BLACK.index);
            // 在样式用应用设置的字体;
            style.setFont(font);
            // 设置自动换行;
            style.setWrapText(true);
            // 设置水平对齐的样式为居中对齐;
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            // 设置垂直对齐的样式为居中对齐;
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM);

            // 设置单元格背景颜色
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            return style;

        }

        /*
         * 列数据信息单元格样式
         */
        public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
            // 设置字体
            HSSFFont font = workbook.createFont();
            // 设置字体大小
            font.setFontHeightInPoints((short) 9);
            // 字体加粗
            // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            // 设置字体名字
            font.setFontName("Times New Roman");
            // 设置样式;
            HSSFCellStyle style = workbook.createCellStyle();
            // 设置底边框;
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            // 设置底边框颜色;
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            // 设置左边框;
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            // 设置左边框颜色;
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            // 设置右边框;
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            // 设置右边框颜色;
            style.setRightBorderColor(HSSFColor.BLACK.index);
            // 设置顶边框;
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            // 设置顶边框颜色;
            style.setTopBorderColor(HSSFColor.BLACK.index);
            // 在样式用应用设置的字体;
            style.setFont(font);
            // 设置自动换行;
            style.setWrapText(true);
            // 设置水平对齐的样式为居中对齐;
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            // 设置垂直对齐的样式为居中对齐;
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM);

            return style;

        }
    }

