package com.yh.ssc.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.yh.ssc.conf.PlatformConfig;
import com.yh.ssc.constants.Common;
import com.yh.ssc.dto.Input;
import com.yh.ssc.dto.QueryData;
import com.yh.ssc.dto.Var;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class SscRealService implements SscService {
    
    @Resource
    private PlatformConfig platformConfig;
    
    @SneakyThrows
    public QueryData query(Integer gameId,Integer rowCnt) {
        URL url = new URL("https://888.jdylwp95.com/APIV2/GraphQL?l=zh-cn&pf=web&udid=654367fe-4fee-498e-8c34-692c748ad437&ac=yehang123");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        
        //ntRbQT4vsgMnQ5dMWu5ORIlunwctDbMjPbqEPU-7U-c=.eyJ1Ijo3OTEzMywiYSI6MjgxNDY3MzUsInQiOiJiNDZkMGFiMTM5ZDc1YjA1IiwiayI6MX0=
        con.setRequestProperty("Authorization",platformConfig.getAuth());
        con.setRequestProperty("Connection","keep-alive");
        con.setRequestProperty("Sec-Fetch-Dest","empty");
        con.setRequestProperty("Sec-Fetch-Mode","cors");
        con.setRequestProperty("Sec-Fetch-Site","same-origin");
        con.setRequestProperty("TE","trailers");
        con.setRequestProperty("Content-Type","application/json");
        
        con.setDoOutput(true);
        
        JSONObject gra = new JSONObject();
        gra.put("query","query GetLotteryCycle($game_id: Int!, $row_count: Int) {\n"
                + "  LotteryGame(game_id: $game_id) {\n" + "    game_id\n" + "    game_value\n" + "    base_game\n"
                + "    official_website\n" + "    lottery_cycle_now {\n" + "      now_cycle_id\n"
                + "      now_cycle_value\n" + "      now_cycle_count_down\n" + "      now_cycle_cool_down\n"
                + "      last_cycle_value\n" + "      last_cycle_game_result\n" + "      future_cycle_list {\n"
                + "        cycle_id\n" + "        cycle_value\n" + "        __typename\n" + "      }\n"
                + "      beforehand_draw {\n" + "        suffix\n" + "        hash\n" + "        __typename\n"
                + "      }\n" + "      __typename\n" + "    }\n"
                + "    lottery_result_history(row_count: $row_count) {\n" + "      cycle_value\n"
                + "      game_result\n" + "      open_time\n" + "      extra_context {\n" + "        hash\n"
                + "        block\n" + "        __typename\n" + "      }\n" + "      beforehand_draw {\n"
                + "        complete\n" + "        suffix\n" + "        hash\n" + "        __typename\n" + "      }\n"
                + "      __typename\n" + "    }\n" + "    extra_info {\n" + "      trxbh_account_from\n"
                + "      trxbh_account_to\n" + "      trxbh_URL\n" + "      __typename\n" + "    }\n"
                + "    __typename\n" + "  }\n" + "}\n");
        
        JSONObject var = new JSONObject();
        var.put("game_id",gameId);
        var.put("row_count",rowCnt);
        gra.put("variables",var);
        
        String graphqlPayload = gra.toJSONString();
        
        try (OutputStream os = con.getOutputStream()) {
            os.write(graphqlPayload.getBytes(StandardCharsets.UTF_8));
        } // 在此自动关闭 OutputStream
        
        // 读取响应
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } // 在此自动关闭 BufferedReader
        
        JSONObject res = JSONObject.parseObject(response.toString());
        if (res.containsKey("data")){
            QueryData queryData = res.getJSONObject("data").getObject("LotteryGame", QueryData.class);
            return queryData;
        }else {
            throw new RuntimeException("queryData error:"+response.toString());
        }
    }
    
    @SneakyThrows
    public String send(Long cycleId,Integer multiple,List<List<Integer>> data) {
        URL url = new URL(Common.ORDER_SEND_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Authorization",platformConfig.getAuth());
        con.setRequestProperty("Connection","keep-alive");
        con.setRequestProperty("Sec-Fetch-Dest","empty");
        con.setRequestProperty("Sec-Fetch-Mode","cors");
        con.setRequestProperty("Sec-Fetch-Site","same-origin");
        con.setRequestProperty("TE","trailers");
        con.setRequestProperty("Content-Type","application/json");

        con.setDoOutput(true);

        JSONObject gra = new JSONObject();
        gra.put("query","mutation AddLotteryOrders($input: [AddLotteryOrderInputObj]!) {\n" +
                "  AddLotteryOrders(orders: $input) {\n" +
                "    message\n" +
                "    order_ids\n" +
                "    __typename\n" +
                "  }\n" +
                "}\n");


        Input input = new Input();
        input.setGame_cycle_id(cycleId);
        input.setBet_multiple(multiple);
        input.setBet_info(JSONArray.toJSONString(data));
        
        List<Input> inputs = new ArrayList<>();
        inputs.add(input);
        Var var = new Var();
        var.setInput(inputs);

        gra.put("variables",var);

        String graphqlPayload = gra.toJSONString();
        
        OutputStream os = con.getOutputStream();
        os.write(graphqlPayload.getBytes("UTF-8"));
        os.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine())!=null){
            response.append(inputLine);
        }

        in.close();;

        log.info("send result:{}",response.toString());
        return response.toString();
    }

    public static void main(String[] args) {
        SscRealService sscDataRealService = new SscRealService();
        List<String> wan = Lists.newArrayList("1","2","3","4","5","6","7","8");
        List<String> qian = Lists.newArrayList();
        List<String> bai = Lists.newArrayList();
        List<String> shi = Lists.newArrayList();
        List<String> ge = Lists.newArrayList();
        List<List<String>> all = Lists.newArrayList();
        all.add(wan);
        all.add(qian);
        all.add(bai);
        all.add(shi);
        all.add(ge);
        
        List<List<Integer>> inte = Lists.newArrayList();
        List<Integer> wanIn = Lists.newArrayList(1,2,3,4,5,6,7,8,9);
        inte.add(wanIn);
        List<Integer> qianIn = Lists.newArrayList();
        List<Integer> baiIn = Lists.newArrayList();
        List<Integer> shiIn = Lists.newArrayList();
        List<Integer> geIn = Lists.newArrayList();
        inte.add(qianIn);
        inte.add(baiIn);
        inte.add(shiIn);
        inte.add(geIn);
        
        System.out.println(JSONArray.toJSONString(inte));
        
//        sscDataRealService.send(24530767L,1,inte);
        
//        QueryData queryData = sscDataRealService.query(190,10);
//        System.out.println(JSONObject.toJSONString(queryData));
    }

}
