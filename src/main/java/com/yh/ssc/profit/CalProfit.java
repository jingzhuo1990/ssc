package com.yh.ssc.profit;

import com.yh.ssc.constants.Common;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-09 10:35
 **/
@Component
public class CalProfit {
    
    public List<Profit> getProfits(Integer round) {
        Double totalCost = 0d;
        List<Profit> profits = new ArrayList<>();
        for (int i=1;i<=round;i++){
            Profit profit = new Profit();
            profit.setRound(i);
            for (int multify=1;multify<100000;multify++){
                Double tempCost = Double.valueOf(multify* Common.SINGLE_AMOUNT);
                totalCost+=tempCost;
                Double tempCurrentProfit = multify * Common.SINGLE_PROFIT;
                Double tempProfit = tempCurrentProfit-totalCost;
                //满足条件，直接返回
                if (tempProfit > 0) {
                    profit.setTotalCost(totalCost);
                    profit.setCost(tempCost);
                    profit.setMultify(multify);
                    profit.setProfit(tempProfit);
                    break;
                }else {
                    totalCost-=tempCost;
                }
            }
            profits.add(profit);
        }
        return profits;
    }
}
