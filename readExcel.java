excel样式
名称	                                层级	上级名称
广东移动	                              1	     1
广东移动/广州市/天河区	                 2	     广东移动
广东移动/广州市/天河区/天河1号          	 3	     广东移动/广州市/天河区
广东移动/广州市/天河区/天河1号/天河4	     4	     广东移动/广州市/天河区/天河1号
广东移动/江门	                          2	       广东移动
广东移动/江门/江门1号	                  3	      广东移动/江门
广东移动/江门/江门1号/江门4	             4	     广东移动/江门/江门1号
广东移动/测试专用/test	                  2	      广东移动
广东移动/测试专用/test	                  3	     广东移动/测试专用/test
广东移动/测试专用/test	                  4	     广东移动/测试专用/test


//用来读excel，然后对名称进行截取
pom.xml要先增加配置：
        <!--读excel使用-->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>3.14</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>3.14</version>
        </dependency>
    </dependencies>
    
public static void main(String[] args) {
        File fl = new File("E:\\文档及资料\\工作文档\\临时文档\\name.xlsx");
        FileInputStream fis;
        try {
            fis = new FileInputStream(fl);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fis);
            //System.out.println(xssfWorkbook.getNumberOfSheets()); //excel的sheet页总数
            XSSFSheet xssfSheet = xssfWorkbook.getSheet("Sheet1");
            HashMap<String,String> splitMap = new HashMap<>();
            //System.out.println(xssfSheet.getLastRowNum());//sheet页的总行数
            for (int num = 1; num <= xssfSheet.getLastRowNum(); num++) {
                XSSFRow xssfRow = xssfSheet.getRow(num);
                String rate = xssfRow.getCell(1).toString(); //层级
                String name = xssfRow.getCell(0).toString(); //当前层级全名
                String upperName = xssfRow.getCell(2).toString(); //上级层级全名
                //System.out.println("rownum=" + xssfRow.getRowNum());//当前的行数
                String rateName1 = "";
                String rateName2 = "";
                String rateName3 = "";
                String rateName4 = "";
                if (rate.equals("1.0")) {
                    rateName1 = name;
                } else if (rate.equals("2.0")) {
                    rateName2 = name.split("/", 2)[1];
                    splitMap.put(name,rateName2+"/");
                } else if (rate.equals("3.0") && name.equals(upperName)) {
                    //当层级为3时,当前层级名称和上级名称一样，则以"/"分割成2个字段值，获取第二个字段值当做子层级
                    rateName3 = name.split("/", 2)[1];
                } else if (rate.equals("3.0")) {
                    rateName3 = name.split(splitMap.get(upperName), 2)[1];
                    splitMap.put(name,rateName3+"/");
                } else if (rate.equals("4.0") && name.equals(upperName)) {
                    rateName4 = xssfRow.getCell(0).toString().split("/", 2)[1];
                    //当层级为4时,当前层级名称和上级名称一样，则以"/"分割成2个字段值，获取第二个字段值当做子层级
                } else if (rate.equals("4.0")) {
                    rateName4 = name.split(splitMap.get(upperName), 2)[1];
                }
                System.out.println("rownum=" + xssfRow.getRowNum() + "  原值= " + rate + "    " + name + "  " + rateName1 + "  " + rateName2 + "  " + rateName3 + "  " + rateName4);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
